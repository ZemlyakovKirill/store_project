package ru.themlyakov.storeproject.repository;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface SimpleRepository<T, R> {

    T insert(@NotNull T t);

    T update(@NotNull T t);

    T delete(@NotNull R id);

    List<T> selectAll();

    T selectById(R id);

}
