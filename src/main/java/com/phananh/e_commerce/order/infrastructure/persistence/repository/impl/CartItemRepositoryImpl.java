package com.phananh.e_commerce.order.infrastructure.persistence.repository.impl;

import com.phananh.e_commerce.order.domain.model.CartItem;
import com.phananh.e_commerce.order.domain.repository.CartItemRepository;
import com.phananh.e_commerce.order.infrastructure.persistence.repository.springdata.SpringDataCartItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CartItemRepositoryImpl implements CartItemRepository {

    SpringDataCartItemRepository springDataCartItemRepository;

    @Override
    public List<CartItem> getByUserId(Long userId) {
        return springDataCartItemRepository.findByUserId(userId);
    }

    @Override
    public Optional<CartItem> getByUserIdAndVariantId(Long userId, Long variantId) {
        return springDataCartItemRepository.getByUserIdAndVariantId(userId, variantId);
    }

    @Override
    public List<CartItem> getByListId(List<Long> ids) {
        return springDataCartItemRepository.findByIdIn(ids);
    }

    @Override
    public Optional<CartItem> getById(Long id) {
        return springDataCartItemRepository.findById(id);
    }

    @Override
    public CartItem save(CartItem cartItem) {
        return springDataCartItemRepository.save(cartItem);
    }

    @Override
    public void delete(CartItem cartItem) {
        springDataCartItemRepository.delete(cartItem);
    }

    @Override
    public void deleteAll(Iterable<CartItem> cartItems) {
        springDataCartItemRepository.deleteAll(cartItems);
    }
}

