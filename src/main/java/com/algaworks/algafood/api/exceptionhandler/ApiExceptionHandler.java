package com.algaworks.algafood.api.exceptionhandler;

import static com.algaworks.algafood.api.exceptionhandler.ProblemType.DADOS_INVALIDOS;
import static com.algaworks.algafood.api.exceptionhandler.ProblemType.ENTIDADE_EM_USO;
import static com.algaworks.algafood.api.exceptionhandler.ProblemType.ERRO_NEGOCIO;
import static com.algaworks.algafood.api.exceptionhandler.ProblemType.MENSAGEM_INCOMPREENSIVEL;
import static com.algaworks.algafood.api.exceptionhandler.ProblemType.RECURSO_NAO_ENCONTRADO;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.algaworks.algafood.core.validator.ValidacaoException;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

	private static final String MSG_ERRO_INESPERADO = "Ocorreu um erro inesperado. Tente novamente e se o erro persistir, entre em contato com o administrador do sistema.";
	LocalDateTime date = LocalDateTime.now();

	@Autowired
	MessageSource messageSource;

	@ExceptionHandler(ValidacaoException.class)
	public ResponseEntity<?> handleValidationExeption(ValidacaoException ex, WebRequest request) {

		String detail = "Um ou mais dados estão inválidos. Faça o preenchimento correto e tente novamente";
		
		HttpStatus status = HttpStatus.BAD_GATEWAY;
		
		List<Problem.Object> objectErrors = ex.getBindingResult().getAllErrors()
											.stream()
											.map(objectError -> {
												
												String nomeCampoErro = objectError.getObjectName();
												
												String messageErro = messageSource.getMessage(objectError, LocaleContextHolder.getLocale());
												
												if(objectError instanceof FieldError) {
													nomeCampoErro = ((FieldError)objectError).getField();
												}
												
												Problem.Object problem =  Problem.Object.builder()
																				 .userMessage(messageErro)
																				 .name(nomeCampoErro)
																				 .build();
												
												return problem;
																				 
												
											}).collect(Collectors.toList());
		
		Problem problem = createProblemBuilder(status.value() , DADOS_INVALIDOS, detail).objects(objectErrors).build();
		
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);

	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		String detail = "O corpo da requisição está inválido. Verifique erros de sintaxe";

		Throwable rootCause = ExceptionUtils.getRootCause(ex);

		if (rootCause instanceof InvalidFormatException) {
			return handleInvalidFormatException((InvalidFormatException) rootCause, headers, status, request);
		} else if (rootCause instanceof PropertyBindingException) {
			return handleUnrecognizedPropertyException((PropertyBindingException) rootCause, headers, status, request);
		} else if (rootCause instanceof DateTimeParseException) {
			return handleDateTimeParseException((DateTimeParseException) rootCause, headers, status, request);
		}
		Problem problem = createProblemBuilder(status.value(), MENSAGEM_INCOMPREENSIVEL, detail).userMessage(detail)
				.build();

		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	private ResponseEntity<Object> handleDateTimeParseException(DateTimeParseException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		
		String detail = "Não foi possível fazer o parse da data: " + ex.getParsedString() + ". Corrija e tente novamente.";
		
		Problem problem = createProblemBuilder(status.value(), DADOS_INVALIDOS, detail)	
										.userMessage(detail)
										.build();

		return handleExceptionInternal(ex, problem, headers, status, request);
		
	}

	@Override
	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		if (ex instanceof MethodArgumentTypeMismatchException) {
			return handleMethodArgumentTypeException((MethodArgumentTypeMismatchException) ex, headers, status,
					request);
		}
		return super.handleTypeMismatch(ex, headers, status, request);
	}

	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		String detail = String.format("O recurso %s que você tentou acessar é inexistente.", ex.getRequestURL());

		Problem problem = createProblemBuilder(status.value(), RECURSO_NAO_ENCONTRADO, detail).userMessage(detail)
				.build();

		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	private ResponseEntity<Object> handleMethodArgumentTypeException(MethodArgumentTypeMismatchException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		ProblemType problemType = ProblemType.PARAMETROS_INVALIDOS;
		String detail = String.format(
				"O parametro de URL: %s recebeu o valor '%s' que é um tipo inválido."
						+ " Corrija e informe um valor do tipo: %s ",
				ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());

		Problem problem = createProblemBuilder(status.value(), problemType, detail)
				.userMessage("A URL acessada está inválida. Revise e tente novamente").build();

		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	/**
	 * Resposta customizada para a Excpetion {@code MethodArgumentNotValidException}
	 * Exception de validação de dados do BeanValidator
	 * 
	 * @param ex      a exceção lançada
	 * @param headers Os headers HTTP a serem inseridos no response
	 * @param status  O HttpStatus do response
	 * @param request A própria requisição (WebRequest)
	 * 
	 * @return Istancia de {@code ResponseEntity}
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		BindingResult bindingResult = ex.getBindingResult();

		/**
		 * Construção dos detalhes de cada validação que falhou. Cada validação falha é
		 * mapeada para um Problem.Field, utilizando o messageSource para pegar a
		 * descrição do erro contido no message.properties
		 *
		 */
		List<Problem.Object> problemObjects = bindingResult.getAllErrors().stream().map(objectError -> {

			String message = messageSource.getMessage(objectError, LocaleContextHolder.getLocale());

			String name = objectError.getObjectName();

			if (objectError instanceof FieldError) {
				name = ((FieldError) objectError).getField();
			}
			return Problem.Object.builder().name(name).userMessage(message).build();

		}).collect(Collectors.toList());

		String detail = String
				.format("Um ou mais dados estão inválidos. Faça o preenchimento correto e tente novamente");

		Problem problem = createProblemBuilder(status.value(), DADOS_INVALIDOS, detail).userMessage(detail)
				.objects(problemObjects).build();

		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	private ResponseEntity<Object> handleUnrecognizedPropertyException(PropertyBindingException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		String path = joinPath(ex.getPath());

		String detail = String
				.format("A propriedade: '%s' não existe. Corrija ou remova essa propriedade e tente novamente", path);

		Problem problem = createProblemBuilder(status.value(), ProblemType.DADOS_INVALIDOS, detail)
				.userMessage(MSG_ERRO_INESPERADO).build();

		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	private ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		palindromo();

		String path = joinPath(ex.getPath());

		String detail = String
				.format("A propriedade '%s' recebeu o valor '%s' que é um tipo inválido. Corrija e informe "
						+ "um valor do tipo %s", path, ex.getValue(), ex.getTargetType().getSimpleName());

		Problem problem = createProblemBuilder(status.value(), MENSAGEM_INCOMPREENSIVEL, detail)
				.userMessage(MSG_ERRO_INESPERADO).build();

		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	@ExceptionHandler(EntidadeNaoEncontradaException.class)
	public ResponseEntity<?> handleEntidadeNaoEncontradaException(Exception ex, WebRequest request) {

		HttpStatus status = HttpStatus.NOT_FOUND;
		String detail = ex.getMessage();
		ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;

		Problem problem = createProblemBuilder(status.value(), problemType, detail).userMessage(detail).build();

		return handleExceptionInternal(ex, problem, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}

	@ExceptionHandler(NegocioException.class)
	public ResponseEntity<?> handleNegocioException(NegocioException ex, WebRequest request) {

		String detail = ex.getMessage();
		Problem problem = createProblemBuilder(BAD_REQUEST.value(), ERRO_NEGOCIO, detail).userMessage(detail).build();

		return handleExceptionInternal(ex, problem, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler(EntidadeEmUsoException.class)
	public ResponseEntity<?> handleEntidadeEmUso(EntidadeEmUsoException ex, WebRequest request) {

		String detail = ex.getMessage();
		Problem problem = createProblemBuilder(CONFLICT.value(), ENTIDADE_EM_USO, detail).userMessage(detail).build();

		return handleExceptionInternal(ex, problem, new HttpHeaders(), HttpStatus.CONFLICT, request);
	}

	@ExceptionHandler(Exception.class)
	private ResponseEntity<?> handleInternalError(Exception ex, WebRequest request) {

		String detail = MSG_ERRO_INESPERADO;
		Problem problem = createProblemBuilder(HttpStatus.INTERNAL_SERVER_ERROR.value(), ProblemType.ERRO_SISTEMA,
				detail).userMessage(MSG_ERRO_INESPERADO).build();
		ex.printStackTrace();
		return handleExceptionInternal(ex, problem, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		if (body == null) {
			body = Problem.builder().timestamp(LocalDateTime.now()).title(status.getReasonPhrase())
					.status(status.value()).userMessage(MSG_ERRO_INESPERADO).build();
		} else if (body instanceof String) {
			body = Problem.builder().timestamp(LocalDateTime.now()).title((String) body).status(status.value())
					.userMessage(MSG_ERRO_INESPERADO).build();
		}
		return super.handleExceptionInternal(ex, body, headers, status, request);
	}

//	private Problem buildProblema(Integer status, String type, String title, String detail) {
//		Problem problema = Problem.builder().status(status).type(type).title(title).detail(detail)
//				.timestamp(LocalDateTime.now()).build();
//		return problema;
//	}

	private Problem.ProblemBuilder createProblemBuilder(Integer status, ProblemType problemType, String detail) {
		return Problem.builder().status(status).type(problemType.getUri()).timestamp(LocalDateTime.now())
				.title(problemType.getTitle()).detail(detail);
	}

	private void palindromo() {
		String palavra = "ivi";
		StringBuilder sbOriginal = new StringBuilder(palavra);
		StringBuilder sb = new StringBuilder();
		for (int i = palavra.length() - 1; i >= 0; i--) {
			sb.append(palavra.charAt(i));
		}

		if (sbOriginal.toString().equals(sb.toString())) {
			System.out.println("Uhu, é palíndromo");
		} else {
			System.out.println("Não é :(");
			System.out.println(sbOriginal);
			System.out.println(sb);
		}
	}

	private String joinPath(List<Reference> path) {
		return path.stream().map(ref -> ref.getFieldName()).collect(Collectors.joining("."));
	}

}
