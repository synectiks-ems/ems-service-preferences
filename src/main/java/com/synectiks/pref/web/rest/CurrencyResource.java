package com.synectiks.pref.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.synectiks.pref.service.CurrencyService;
import com.synectiks.pref.service.dto.CurrencyDTO;
import com.synectiks.pref.web.rest.errors.BadRequestAlertException;
import com.synectiks.pref.web.rest.util.HeaderUtil;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.synectiks.pref.domain.Currency}.
 */
@RestController
@RequestMapping("/api")
public class CurrencyResource {

    private final Logger log = LoggerFactory.getLogger(CurrencyResource.class);

    private static final String ENTITY_NAME = "currency";

    
    private String applicationName;

    private final CurrencyService currencyService;

    public CurrencyResource(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    /**
     * {@code POST  /currencies} : Create a new currency.
     *
     * @param currencyDTO the currencyDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new currencyDTO, or with status {@code 400 (Bad Request)} if the currency has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/currencies")
    public ResponseEntity<CurrencyDTO> createCurrency(@RequestBody CurrencyDTO currencyDTO) throws URISyntaxException {
        log.debug("REST request to save Currency : {}", currencyDTO);
        if (currencyDTO.getId() != null) {
            throw new BadRequestAlertException("A new currency cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CurrencyDTO result = currencyService.save(currencyDTO);
        return ResponseEntity.created(new URI("/api/currencies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /currencies} : Updates an existing currency.
     *
     * @param currencyDTO the currencyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated currencyDTO,
     * or with status {@code 400 (Bad Request)} if the currencyDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the currencyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/currencies")
    public ResponseEntity<CurrencyDTO> updateCurrency(@RequestBody CurrencyDTO currencyDTO) throws URISyntaxException {
        log.debug("REST request to update Currency : {}", currencyDTO);
        if (currencyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CurrencyDTO result = currencyService.save(currencyDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, currencyDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /currencies} : get all the currencies.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of currencies in body.
     */
    @GetMapping("/currencies")
    public List<CurrencyDTO> getAllCurrencies() {
        log.debug("REST request to get all Currencies");
        return currencyService.findAll();
    }

    /**
     * {@code GET  /currencies/:id} : get the "id" currency.
     *
     * @param id the id of the currencyDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the currencyDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/currencies/{id}")
    public ResponseEntity<CurrencyDTO> getCurrency(@PathVariable Long id) {
        log.debug("REST request to get Currency : {}", id);
        Optional<CurrencyDTO> currencyDTO = currencyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(currencyDTO);
    }

    /**
     * {@code DELETE  /currencies/:id} : delete the "id" currency.
     *
     * @param id the id of the currencyDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/currencies/{id}")
    public ResponseEntity<Void> deleteCurrency(@PathVariable Long id) {
        log.debug("REST request to delete Currency : {}", id);
        currencyService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/currencies?query=:query} : search for the currency corresponding
     * to the query.
     *
     * @param query the query of the currency search.
     * @return the result of the search.
     */
    @GetMapping("/_search/currencies")
    public List<CurrencyDTO> searchCurrencies(@RequestParam String query) {
        log.debug("REST request to search Currencies for query {}", query);
        return currencyService.search(query);
    }

}
