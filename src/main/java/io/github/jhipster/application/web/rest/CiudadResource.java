package io.github.jhipster.application.web.rest;

import io.github.jhipster.application.domain.Ciudad;
import io.github.jhipster.application.service.CiudadService;
import io.github.jhipster.application.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.application.service.dto.CiudadCriteria;
import io.github.jhipster.application.service.CiudadQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link io.github.jhipster.application.domain.Ciudad}.
 */
@RestController
@RequestMapping("/api")
public class CiudadResource {

    private final Logger log = LoggerFactory.getLogger(CiudadResource.class);

    private static final String ENTITY_NAME = "ciudad";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CiudadService ciudadService;

    private final CiudadQueryService ciudadQueryService;

    public CiudadResource(CiudadService ciudadService, CiudadQueryService ciudadQueryService) {
        this.ciudadService = ciudadService;
        this.ciudadQueryService = ciudadQueryService;
    }

    /**
     * {@code POST  /ciudads} : Create a new ciudad.
     *
     * @param ciudad the ciudad to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ciudad, or with status {@code 400 (Bad Request)} if the ciudad has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ciudads")
    public ResponseEntity<Ciudad> createCiudad(@RequestBody Ciudad ciudad) throws URISyntaxException {
        log.debug("REST request to save Ciudad : {}", ciudad);
        if (ciudad.getId() != null) {
            throw new BadRequestAlertException("A new ciudad cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Ciudad result = ciudadService.save(ciudad);
        return ResponseEntity.created(new URI("/api/ciudads/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ciudads} : Updates an existing ciudad.
     *
     * @param ciudad the ciudad to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ciudad,
     * or with status {@code 400 (Bad Request)} if the ciudad is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ciudad couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ciudads")
    public ResponseEntity<Ciudad> updateCiudad(@RequestBody Ciudad ciudad) throws URISyntaxException {
        log.debug("REST request to update Ciudad : {}", ciudad);
        if (ciudad.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Ciudad result = ciudadService.save(ciudad);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ciudad.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /ciudads} : get all the ciudads.
     *
     * @param pageable the pagination information.
     * @param queryParams a {@link MultiValueMap} query parameters.
     * @param uriBuilder a {@link UriComponentsBuilder} URI builder.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ciudads in body.
     */
    @GetMapping("/ciudads")
    public ResponseEntity<List<Ciudad>> getAllCiudads(CiudadCriteria criteria, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get Ciudads by criteria: {}", criteria);
        Page<Ciudad> page = ciudadQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /ciudads/count} : count all the ciudads.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/ciudads/count")
    public ResponseEntity<Long> countCiudads(CiudadCriteria criteria) {
        log.debug("REST request to count Ciudads by criteria: {}", criteria);
        return ResponseEntity.ok().body(ciudadQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ciudads/:id} : get the "id" ciudad.
     *
     * @param id the id of the ciudad to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ciudad, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ciudads/{id}")
    public ResponseEntity<Ciudad> getCiudad(@PathVariable Long id) {
        log.debug("REST request to get Ciudad : {}", id);
        Optional<Ciudad> ciudad = ciudadService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ciudad);
    }

    /**
     * {@code DELETE  /ciudads/:id} : delete the "id" ciudad.
     *
     * @param id the id of the ciudad to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ciudads/{id}")
    public ResponseEntity<Void> deleteCiudad(@PathVariable Long id) {
        log.debug("REST request to delete Ciudad : {}", id);
        ciudadService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/ciudads?query=:query} : search for the ciudad corresponding
     * to the query.
     *
     * @param query the query of the ciudad search.
     * @param pageable the pagination information.
     * @param queryParams a {@link MultiValueMap} query parameters.
     * @param uriBuilder a {@link UriComponentsBuilder} URI builder.
     * @return the result of the search.
     */
    @GetMapping("/_search/ciudads")
    public ResponseEntity<List<Ciudad>> searchCiudads(@RequestParam String query, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to search for a page of Ciudads for query {}", query);
        Page<Ciudad> page = ciudadService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}