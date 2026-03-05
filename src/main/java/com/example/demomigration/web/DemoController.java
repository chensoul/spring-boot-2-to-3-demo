package com.example.demomigration.web;

import com.example.demomigration.persistence.Item;
import com.example.demomigration.persistence.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * Covers: @Autowired on constructor, @RequestMapping(method=), @PathVariable("id"),
 * HttpStatus, APPLICATION_JSON_UTF8_VALUE, javax.validation.
 */
@RestController
@RequestMapping("/api")
public class DemoController {

    private final ItemRepository itemRepository;

    @Autowired
    public DemoController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @RequestMapping(value = "/public/items", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<Item>> listItems() {
        return new ResponseEntity<>(itemRepository.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "/public/items/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Item> getItem(@PathVariable("id") Long id) {
        return itemRepository.findById(id)
                .map(item -> new ResponseEntity<>(item, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/public/items", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Item> createItem(@Valid @RequestBody ItemRequest request) {
        Item item = new Item();
        item.setName(request.getName());
        return new ResponseEntity<>(itemRepository.save(item), HttpStatus.CREATED);
    }

    public static class ItemRequest {
        @NotBlank
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
