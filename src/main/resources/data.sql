INSERT INTO tipos_ubicacion
(`nidtipo_ubicacion`,
`vcdescripcion`,
`vctipo`)
VALUES
(1, 'tipo ubicacion de prueba', 'Obra');

INSERT INTO ubicaciones
(`nidubicacion`,
`vcciudad`,
`vcdescripcion`,
`vcdireccion`,
`nidtipo_ubicacion`
)
VALUES
(1, 'Cali', 'Obra Sur', 'Calle Cra', 1);

INSERT INTO familias
(`nidfamilia`,
`vcnombre`,
`vcdescripcion`)
VALUES
(1, 'familia1', 'descrip');

INSERT INTO fabricantes
(`vcnitfabricante`,
`vcnombre`,
`vcdescripcion`)
VALUES
('11111','Fabricante prueba','Prueba');

INSERT INTO estados
(`nidestado`,
`vcdescripcion`,
`vctipo`)
VALUES
(1,'Bueno','Bueno'),
(2,'Regular','Regular'),
(3,'Malo','Malo')
;

INSERT INTO tipos_empresa
(`nidtipo_empresa`,
`vcdescripcion`,
`vctipo`)
VALUES
(1,'Admin','Admin'),
(2,'Owner','Owner'),
(3,'Cliente','Cliente');

INSERT INTO empresas
(`vcnitempresa`,
`vcdescripcion`,
`vcnombre`,
`nidtipo_empresa`)
VALUES
('1111','Prueba','Empresa Prueba',3);

INSERT INTO productos
(`vccodigopieza`,
`vcarea`,
`vcdescripcion`,
`vcorden`,
`vcnit_empresa`,
`nidestado`,
`vcnitfabricante`,
`nidfamilia`,
`nidubicacion`)
VALUES
('1111', 'a1', 'prueba', 'OR1', '1111', 1, '11111', 1, 1),
('1112', 'a1', 'prueba', 'OR1', '1111', 1, '11111', 1, 1),
('1113', 'a1', 'prueba', 'OR1', '1111', 1, '11111', 1, 1),
('1114', 'a1', 'prueba', 'OR1', '1111', 1, '11111', 1, 1),
('1115', 'a1', 'prueba', 'OR1', '1111', 1, '11111', 1, 1);