package com.algaworks.wine.controller;

import java.util.List;

import javax.validation.Valid;

import com.algaworks.wine.model.TipoVinho;
import com.algaworks.wine.model.Vinho;
import com.algaworks.wine.repository.Vinhos;
import com.algaworks.wine.service.CadastroVinhoService;
import com.algaworks.wine.storage.FotoStorage;
import com.algaworks.wine.storage.FotoStorageS3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/vinhos")
public class VinhosController {

	@Autowired
	private Vinhos vinhos;

	@Autowired
	private CadastroVinhoService cadastroVinhosService;

	@Autowired
	private FotoStorage fotoStorage;

	@RequestMapping("/novo")
	public ModelAndView novo(Vinho vinho) {
		ModelAndView mv = new ModelAndView("/vinho/CadastroVinho");
		mv.addObject("tipos", TipoVinho.values());
		return mv;
	}

	@RequestMapping
	public ModelAndView pesquisa() {
		ModelAndView mv = new ModelAndView("/vinho/ListagemVinhos");
		mv.addObject("vinhos", vinhos.findAll());
		return mv;
	}

	@RequestMapping(value = "/novo", method = RequestMethod.POST)
	public ModelAndView salvar(@Valid Vinho vinho, BindingResult result, RedirectAttributes attributes){
		if(result.hasErrors()){
			return novo(vinho);
		}
		cadastroVinhosService.salvar(vinho);
		attributes.addFlashAttribute("mensagem","Vinho salvo com sucesso!");
		return new ModelAndView("redirect:/vinhos/novo");
	}

	@RequestMapping("/{codigo}")
	public ModelAndView visualizar(@PathVariable("codigo") Vinho vinho){
		ModelAndView mv = new ModelAndView("/vinho/VisualizacaoVinho");

		if(vinho.temFoto()){
			vinho.setUrl(fotoStorage.getUrl(vinho.getFoto()));
		}
		//Vinho vinho = vinhos.findOne(codigo);
		mv.addObject("vinho", vinho);
		return mv;
	}
	

}
