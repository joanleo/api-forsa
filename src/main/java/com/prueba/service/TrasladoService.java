package com.prueba.service;

import java.text.ParseException;
import java.time.LocalDate;

import org.springframework.data.domain.Page;

import com.prueba.dto.TrasladoDTO;
import com.prueba.entity.Traslado;

public interface TrasladoService {

	TrasladoDTO create(TrasladoDTO trasladoDTO);

	Traslado getTraslado(Long id);

	Page<Traslado> findBetweenDates(LocalDate desde, LocalDate hasta, int offset, int pageSize);

	/**
	 * @param idtraslado
	 * @param codigopieza
	 * @return
	 * @throws ParseException 
	 * @throws Exception 
	 */
	public Traslado confirmarPieza(Long idtraslado, String codigopieza) throws Exception;

	/**
	 * @param idtraslado
	 * @param codigopieza
	 * @return
	 * @throws Exception 
	 */
	public Traslado recibirPieza(Long idtraslado, String codigopieza) throws Exception;

	/**
	 * @param idtraslado
	 * @return
	 */
	public Traslado confirmarTodo(Long idtraslado);

	/**
	 * @param idtraslado
	 * @return
	 */
	public Traslado recibirTodo(Long idtraslado);

	/**
	 * @param idtraslado
	 * @param codigopieza
	 */
	public void eliminarPieza(Long idtraslado, String codigopieza);

	/**
	 * @param idtraslado
	 */
	public void eliminarTodo(Long idtraslado);
	
	/**
	 * 
	 * @param idtraslado
	 * @param nit 
	 */
	public void eliminarTraslado(Long idtraslado, Long nit);

	/**
	 * @param pagina
	 * @param items
	 * @return
	 */
	public Page<Traslado> buscarTraslados(Integer pagina, Integer items);

	/**
	 * @param trasladoDTO
	 * @param pagina
	 * @param items
	 * @return
	 */
	public Page<Traslado> buscarTraslados(TrasladoDTO trasladoDTO, Integer pagina, Integer items);



}
