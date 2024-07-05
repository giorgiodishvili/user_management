package com.gv.user.management.config;

import org.mapstruct.MapperConfig;

import static org.mapstruct.MappingInheritanceStrategy.EXPLICIT;
import static org.mapstruct.ReportingPolicy.ERROR;

/**
 * use mapstruct whenever possible
 */
@MapperConfig(componentModel = "spring", unmappedTargetPolicy = ERROR, mappingInheritanceStrategy = EXPLICIT)
public interface MapStructConfig {}
