INSERT INTO m_tipos_empresa
(`nidtipo_empresa`,
`vcnombre`)
VALUES
(1,'Admin'),
(2,'Owner'),
(3,'Cliente');

INSERT INTO m_empresas
(`nid_empresa`,
`vcnitempresa`,
`vcnombre`,
`nidtipo_empresa`)
VALUES
(1,'2222','Metrolink sas',1);

INSERT INTO m_tipos_ubicacion
(`nidtipo_ubicacion`,
`vcnombre`,
`vcnitempresa`)
VALUES
(1, 'Obra', '2222'),
(2, 'Almacen mercadeo', '2222');

INSERT INTO m_ubicaciones
(`nidubicacion`,
`vcciudad`,
`vcdireccion`,
`vcnombre`,
`nidtipo_ubicacion`,
`vcnitempresa`
)
VALUES
(1, 'Cali', 'Cra 50 # 13b-83', 'Tribeka', 1, '2222'),
(2, 'Guachene', 'Zona Franca Permanente del Cauca- Etapa 1', 'Showroom', 2, '2222');

INSERT INTO m_familias
(`nidfamilia`,
`vcnombre`,
`vcsigla`,
`vcnitempresa`
)
VALUES
(1, 'Formaletas para muro', 'FM', '2222'),
(2, 'Formaleta unión muro losa', 'UM', '2222'),
(3, 'Formaletas para losas', 'FL', '2222'),
(4, 'Formaletas para Culata', 'CL', '2222'),
(5, 'Formaletas para escalera', 'SC', '2222'),
(6, 'Formaletas para punto fijo', 'FP', '2222'),
(10, 'Accesorios', 'AC', '2222');

INSERT INTO m_fabricantes
(`nid_fabricante`,
`vcnitfabricante`,
`vcnombre`,
`vcnitempresa`
)
VALUES
(1,'11111','Forsa sa', '2222');

INSERT INTO m_estados
(`nidestado`,
`vcnombre`,
`vcnitempresa`)
VALUES
(1,'Bueno', '2222'),
(2,'Regular', '2222'),
(3,'Malo', '2222');

INSERT INTO m_usuarios
(`nidusuario`,
`vcemail`,
`vcnombre`,
`vcpassword`,
`vcusername`,
`vcnitempresa`,
`nidrol`)
VALUES
(1,'jleon@metrolinkeu.com', 'Joan David Leon', '$2a$10$wTWky7f3N.ASn.Q1VN0u/e3q2.cIo/5A4C8XHFFUM7oiZLNbmF9GO', 'joan1', '2222',1)