package com.group4.Inmobiliaria.controller;

import com.group4.Inmobiliaria.entidades.Ente;

import com.group4.Inmobiliaria.entidades.Cita;
import com.group4.Inmobiliaria.entidades.Oferta;
import com.group4.Inmobiliaria.entidades.Propiedad;
import com.group4.Inmobiliaria.entidades.Usuario;
import com.group4.Inmobiliaria.excepciones.MiExcepcion;
import com.group4.Inmobiliaria.service.ImagenService;
import com.group4.Inmobiliaria.service.OfertaService;
import com.group4.Inmobiliaria.service.PropiedadService;
import com.group4.Inmobiliaria.utils.Image;
import java.util.List;
import java.util.stream.Collectors;

import com.group4.Inmobiliaria.utils.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/propiedad")
@Controller
public class PropiedadController {

    @Autowired
    PropiedadService propiedadService;

    @Autowired
    OfertaService ofertaService;
    
    @Autowired
    ImagenService imagenService;
    
    @GetMapping("/carga")
    public String cargarPropiedad(Propiedad propiedad, Model model) {
        Usuario propietario = ((Usuario) Session.getUserSession());

        propiedad.setPropietario((Ente) propietario);

        model.addAttribute("propiedad", propiedad);
        return "carga";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Propiedad propiedad) throws Exception {

        List<MultipartFile> imagenesFiles = propiedad.getImagenesFiles();

        propiedad.setDisponible(true);

        Propiedad propiedadSaved = propiedadService.guardar(propiedad);

        if(imagenesFiles != null) imagenService.guardarImagenesPropiedad(imagenesFiles, propiedadSaved);

        return "redirect:/";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable("id") String id, Model model) {
        Propiedad propiedad = propiedadService.encontrarById(id);

        List<String> imagenes = Image.getBase64Images(imagenService.findImagesByPropiedadId(id));

        model.addAttribute("propiedad", propiedad);
        model.addAttribute("imagenes", imagenes);
        return "carga";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable("id") String id) throws MiExcepcion {
        Propiedad propiedad = propiedadService.encontrarById(id);
        propiedad.setDisponible(false);
        propiedadService.guardar(propiedad);
        return "redirect:/vendedor";
    }

    @GetMapping("/publicar/{id}")
    public String publicar(@PathVariable("id") String id) throws MiExcepcion {
        Propiedad propiedad = propiedadService.encontrarById(id);
        propiedad.setDisponible(true);
        propiedadService.guardar(propiedad);
        return "redirect:/vendedor";
    }

    @GetMapping("/all")
    public String listar(Model model) {
        List<Propiedad> propiedades = propiedadService.findAlldWithImages();
        propiedades = propiedades.stream().filter(prop -> prop.isDisponible()).collect(Collectors.toList());
        model.addAttribute("propiedades", propiedades);
        return "propiedades";
    }
       
    @GetMapping("/{id}")
    public String mostrarDetallePropiedad(@PathVariable("id") String id, Model model) {


        List<Propiedad> propiedades = propiedadService.findAlldWithImages();

        propiedades = propiedades.stream().filter(prop -> prop.getId().equals(id)).collect(Collectors.toList());

        Propiedad propiedad = propiedades.get(0);

        Usuario usuario = Session.getUserSession();
        Oferta oferta = new Oferta();
        Cita cita = new Cita();
        model.addAttribute("propiedad", propiedad);
        model.addAttribute("oferta", oferta);
        model.addAttribute("usuario", usuario);
        model.addAttribute("cita", cita);
        return "propiedad";
    }

}
