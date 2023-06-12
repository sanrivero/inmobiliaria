/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.group4.Inmobiliaria.repository;

import com.group4.Inmobiliaria.entidades.Solicitud;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author PC - Escritorio
 */
@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud, String>{
    public List<Solicitud>findByEmisor_id(String id);
    
    public List<Solicitud>findByReceptor_id(String id);
}
