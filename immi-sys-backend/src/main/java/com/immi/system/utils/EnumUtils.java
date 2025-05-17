package com.immi.system.utils;

import com.immi.system.DTOs.EnumValueDTO;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EnumUtils {
    public static <E extends Enum<E>> List<EnumValueDTO> convertEnumToDTO(Class<E> enumClass) {
        return Arrays.stream(enumClass.getEnumConstants())
            .map(e -> new EnumValueDTO(e.name(), e.toString()))
            .collect(Collectors.toList());
    }
}