package com.andjela.diplomski.service;

import com.andjela.diplomski.dto.cake.CakeCreateDto;
import com.andjela.diplomski.dto.cake.CakeDto;
import com.andjela.diplomski.dto.cake.CakeMapper;
import com.andjela.diplomski.dto.cake.CakeUpdateDto;
import com.andjela.diplomski.entity.Cake;
import com.andjela.diplomski.entity.codebook.Category;
import com.andjela.diplomski.exception.DataNotValidException;
import com.andjela.diplomski.exception.ResourceNotFoundException;
import com.andjela.diplomski.repository.CakeRepository;
import com.andjela.diplomski.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CakeService implements ICakeService {
    private final CakeRepository cakeRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public CakeDto createCake(CakeCreateDto cakeCreateDto) {
        Category category = categoryRepository.findById(cakeCreateDto.getCategoryId()).orElseThrow(() -> new ResourceNotFoundException("Didnt find category with id: " + cakeCreateDto.getCategoryId()));
        Cake cake = Cake.builder()
                .title(cakeCreateDto.getTitle())
                .pricePerKilo(cakeCreateDto.getPricePerKilo())
                .decorationPrice(cakeCreateDto.getDecorationPrice())
                .minWeight(cakeCreateDto.getMinWeight())
                .maxWeight(cakeCreateDto.getMaxWeight())
                .minTier(cakeCreateDto.getMinTier())
                .maxTier(cakeCreateDto.getMaxTier())
                .imageUrl(cakeCreateDto.getImageUrl())
                .category(category)
                .createdAt(LocalDateTime.now())
                .build();

        cakeRepository.save(cake);

        return CakeMapper.MAPPER.mapToCakeDto(cake);
    }

    @Override
    public CakeDto getCakeById(Long cakeId) {
        Cake cake = cakeRepository.findById(cakeId).orElseThrow(() -> new ResourceNotFoundException("Didn't find cake with id:" + cakeId));
        return CakeMapper.MAPPER.mapToCakeDto(cake);
    }

    @Override

    public List<CakeDto> getCakes() {
        List<CakeDto> foundCakes = new ArrayList<>();
        List<Cake> cakes = cakeRepository.findAll();

        for (Cake cake : cakes) {
            CakeDto cakeDto = CakeMapper.MAPPER.mapToCakeDto(cake);
            foundCakes.add(cakeDto);
        }
        return foundCakes;
    }

    @Transactional
    @Override
    public CakeDto updateCake(Long id, CakeUpdateDto cakeUpdateDto) {
        Cake foundCake = cakeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Entity whit id " + id + " could not be updated"));
        Category category = categoryRepository.findById(cakeUpdateDto.getCategoryId()).orElseThrow(() -> new ResourceNotFoundException("Didn't find category with id: " + cakeUpdateDto.getCategoryId()));

        foundCake.setTitle(cakeUpdateDto.getTitle());
        foundCake.setPricePerKilo(cakeUpdateDto.getPricePerKilo());
        foundCake.setDecorationPrice(cakeUpdateDto.getDecorationPrice());
        foundCake.setMinWeight(cakeUpdateDto.getMinWeight());
        foundCake.setMaxWeight(cakeUpdateDto.getMaxWeight());
        foundCake.setMinTier(cakeUpdateDto.getMinTier());
        foundCake.setMaxTier(cakeUpdateDto.getMaxTier());
        foundCake.setImageUrl(cakeUpdateDto.getImageUrl());
        foundCake.setCategory(category);
        foundCake.setUpdatedAt(LocalDateTime.now());

        cakeRepository.save(foundCake);

        return CakeMapper.MAPPER.mapToCakeDto(foundCake);
    }

    @Override
    public void deleteCake(Long cakeId) {
        Optional<Cake> cakeOptional = cakeRepository.findById(cakeId);
        if (!cakeOptional.isPresent()) {
            throw new ResourceNotFoundException("Didn't find cake with id: " + cakeId);
        }
        Cake cake = cakeOptional.get();
        cake.setCategory(null);
        cakeRepository.delete(cake);
    }


    @Override
    public List<CakeDto> findCakeByCategory(String category) {
        List<Cake> cakes = cakeRepository.findByCategory(category);
        return CakeMapper.MAPPER.mapToListCakeDto(cakes);
    }

    @Override
    public List<CakeDto> searchCakes(String query) {
        List<Cake> cakes = cakeRepository.searchCakes(query);
        return CakeMapper.MAPPER.mapToListCakeDto(cakes);
    }

    @Override
    public Page<Cake> getAllCakesPageable(String category, int minWeight, int maxWeight, int minTier, int maxTier, String sort, Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sort));

        List<Cake> cakes = cakeRepository.filterCakes(category, minWeight, maxWeight, minTier, maxTier, sort);

        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), cakes.size());

        List<Cake> pageContent = cakes.subList(startIndex, endIndex);
        return new PageImpl<>(pageContent, pageable, cakes.size());
    }

    @Override
    public List<CakeDto> getAllCakes() {
        List<Cake> cakes = cakeRepository.findAll();
        return CakeMapper.MAPPER.mapToListCakeDto(cakes);
    }

}
