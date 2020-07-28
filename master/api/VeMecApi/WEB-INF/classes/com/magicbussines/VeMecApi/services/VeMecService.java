package com.magicbussines.VeMecApi.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.magicbussines.VeMecApi.interfaceServices.IVeMecService;
import com.magicbussines.VeMecApi.interfaces.IVeMec;
import com.magicbussines.VeMecApi.models.Paciente;
import com.magicbussines.VeMecApi.models.VeMec;
@Service
public class VeMecService implements IVeMecService {
	
	@Autowired
	private IVeMec data;
	@Override
	public List<VeMec> listar() {
		// TODO Auto-generated method stub
		return (List<VeMec>) data.findAll();
	}

	@Override
	public Optional<VeMec> obtener(String id) {
		// TODO Auto-generated method stub
		return data.findById(id);
	}


	@Override
	public VeMec save(VeMec vm) {
		// TODO Auto-generated method stub
		VeMec vmupd = data.save(vm);
		return vmupd;
	}

	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub
		data.deleteById(id);		
	}
	
	@Override
	public Page<VeMec> listarPaged(int page, int sizeOf) {
		// TODO Auto-generated method stub
		Pageable paging = PageRequest.of(page, sizeOf);
		return data.findAll(paging);
	}

}
