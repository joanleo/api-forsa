INSERT INTO m_tipos_empresa
(`nidtipo_empresa`,
`vcnombre`)
VALUES
(1,'Admin'),
(2,'Owner'),
(3,'Cliente');

INSERT INTO m_empresas
(`vcnitempresa`,
`vcnombre`,
`nidtipo_empresa`)
VALUES
('1111','Empresa Prueba X',3),
('23456','Empresa Prueba Y',3),
('34567','Empresa Prueba Z',3),
('2222','Metrolink sas',1);

INSERT INTO m_tipos_ubicacion
(`nidtipo_ubicacion`,
`vcnombre`,
`vcnitempresa`)
VALUES
(1, 'Obra', '2222');

INSERT INTO m_ubicaciones
(`nidubicacion`,
`vcciudad`,
`vcdireccion`,
`vcnombre`,
`nidtipo_ubicacion`,
`vcnitempresa`
)
VALUES
(1, 'Cali', 'Calle Cra', 'Las flores 1', 1, '2222'),
(2, 'Cali', 'Calle Cra', 'Las avenidas 1', 1, '2222');

INSERT INTO m_familias
(`nidfamilia`,
`vcnombre`,
`vcnitempresa`
)
VALUES
(1, 'Formaletas para muro', '2222'),
(2, 'Formaleta unión muro losa', '2222'),
(3, 'Formaletas para losas', '2222'),
(4, 'Formaletas para Culata', '2222'),
(5, 'Formaletas para escalera', '2222'),
(6, 'Formaletas para punto fijo', '2222'),
(10, 'Accesorios', '2222');

INSERT INTO m_fabricantes
(`vcnitfabricante`,
`vcnombre`,
`vcnitempresa`
)
VALUES
('11111','Fabricante prueba', '2222'),
('12345', 'Formaletas XX', '2222');

INSERT INTO m_estados
(`nidestado`,
`vcnombre`,
`vcnitempresa`)
VALUES
(1,'Bueno', '2222'),
(2,'Regular', '2222'),
(3,'Malo', '2222');

INSERT INTO mov_activos
(`vccodigopieza`,
`narea`,
`vcnombre`,
`vcorden`,
`vcnitempresa`,
`nidestado`,
`vcnitfabricante`,
`nidfamilia`,
`nidubicacion`)
VALUES
('1111', 22.4, 'prueba', 'OR1', '2222', 1, '11111', 1, 1),
('1112', 74.55, 'prueba', 'OR1', '23456', 1, '12345', 1, 1),
('1113', 97, 'prueba', 'OR1', '2222', 1, '11111', 1, 1),
('1114', 99, 'prueba', 'OR5', '23456', 1, '12345', 1, 1),
('1115', 223, 'prueba', 'OR6', '2222', 1, '11111', 1, 1),
('8484', 22.12, 'prueba', 'OR4', '23456', 1, '12345', 1, 1),
('64785', 74.2, 'prueba', 'OR3', '1111', 1, '11111', 1, 1),
('37585', 9.7, 'prueba', 'OR2', '23456', 1, '11111', 1, 1),
('14256', 8.3, 'prueba', 'OR2', '1111', 1, '12345', 1, 1);