package com.magicbussines.VeMecApi.interfaces;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.magicbussines.VeMecApi.models.VeMec;

public interface IVeMec extends PagingAndSortingRepository<VeMec, String> {

}
