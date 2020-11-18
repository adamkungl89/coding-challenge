package com.adamspokes.demo;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

public final class TypeConvertHelpers {
    
    public static String resourceToString(Resource resource) {
        try (Reader reader = new InputStreamReader(resource.getInputStream())) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
	}

	public static String moneyNumberFormat(BigDecimal num) {
        StringBuilder out = new StringBuilder();
        //remove cents
        int noCents = num.intValue();
        String stringNoCents = Integer.toString(noCents);
        out.append(stringNoCents);
        //add commas
        int x = 1;
        int len = out.length();
        while (3*x < len) {
            out.insert(len-3*x, ",");
            x++;
        }
        //add $
        out.insert(0, "$");
		return out.toString();
    }
    
    public static String percentageNumberFormat(BigDecimal num) {
        StringBuilder out = new StringBuilder();
        //remove decimal digit
        num = num.setScale(1, RoundingMode.HALF_UP);
        String stringNoCents = num.toString();
        out.append(stringNoCents);
        //add %
        out.append("%");
		return out.toString();
    }
}
