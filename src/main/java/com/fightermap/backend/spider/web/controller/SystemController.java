package com.fightermap.backend.spider.web.controller;

import com.fightermap.backend.spider.common.exception.NotFoundException;
import com.fightermap.backend.spider.core.model.entity.Area;
import com.fightermap.backend.spider.core.repository.AreaRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

/**
 * @author zengqk
 */
@RequestMapping(path = "/system", produces = APPLICATION_JSON_UTF8_VALUE)
@RestController
public class SystemController {
    private final AreaRepository areaRepository;

    public SystemController(AreaRepository areaRepository) {
        this.areaRepository = areaRepository;
    }

    @GetMapping(path = "/area")
    public Area getArea(Long id) {
        throw new NotFoundException("Not found test.");
    }

}
