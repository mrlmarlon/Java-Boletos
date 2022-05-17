package br.com.java_brasil.boleto.util;

import br.com.java_brasil.boleto.exception.BoletoException;
import br.com.java_brasil.boleto.model.BoletoModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ValidaUtils {

    /**
     * Valida o Objeto Configuracao
     * @param configuracao
     * @param <T>
     */
    public static <T> void validaConfiguracao(T configuracao) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        StringBuilder erros = new StringBuilder();
        for (ConstraintViolation<T> violation : validator.validate(configuracao)) {
            erros.append("Campo ")
                    .append(violation.getPropertyPath())
                    .append(" ")
                    .append(violation.getMessage())
                    .append(".");
        }
        if (StringUtils.isNotBlank(erros.toString())) {
            throw new BoletoException(erros.toString());
        }
    }

    /**
     * Valida o Objeto Boleto
     * @param boleto
     * @param camposObrigatorios
     */
    public static void validaBoletoModel(@NonNull BoletoModel boleto, List<String> camposObrigatorios) {
        StringBuilder erros = new StringBuilder();
        ObjectMapper objectMapper = JsonMapper.builder()
                .findAndAddModules()
                .build();

        Map<?,?> props = objectMapper.convertValue(boleto, Map.class);

        props.forEach((key, value) -> {
            if (value instanceof Map) {
                ((Map<?,?>) value).forEach((k, v) -> {
                    if (camposObrigatorios.contains(key + "." + k) && !verifica(v).isPresent()) {
                        erros.append("Campo ")
                                .append(key).append(".").append(k)
                                .append(" não pode estar vazio.");
                    }
                });
            } else {
                if (camposObrigatorios.contains(key.toString()) && !verifica(value).isPresent()) {
                    erros.append("Campo ")
                            .append(key)
                            .append(" não pode estar vazio.");
                }
            }
        });

        if (StringUtils.isNotBlank(erros.toString())) {
            throw new BoletoException(erros.toString());
        }
    }

    /**
     * Verifica se um objeto é vazio.
     *
     * @param obj
     * @return <b>true</b> se o objeto for vazio(empty).
     */
    public static <T> Optional<T> verifica(T obj) {
        if (obj == null)
            return Optional.empty();
        if (obj instanceof Collection)
            return ((Collection<?>) obj).size() == 0 ? Optional.empty() : Optional.of(obj);

        final String s = String.valueOf(obj).trim();

        return s.length() == 0 || s.equalsIgnoreCase("null") ? Optional.empty() : Optional.of(obj);
    }
}
