package com.cinema.catalog.application.dto;

import com.cinema.catalog.domain.Screening;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface ScreeningMapper {

    ScreeningDto mapToDto(Screening screening);

    List<ScreeningDto> mapToDto(List<Screening> screening);
}
