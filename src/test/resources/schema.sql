-- Tabla cliente
CREATE TABLE PUBLIC.CLIENTE (
                                ID_CLIENTE SERIAL NOT NULL,
                                TIPO_IDENTIFICACION VARCHAR(5) NOT NULL,
                                NUMERO_IDENTIFICACION VARCHAR(15) NOT NULL,
                                NOMBRES VARCHAR(50) NOT NULL,
                                APELLIDOS VARCHAR(50) NOT NULL,
                                CORREO_ELECTRONICO VARCHAR(50) NOT NULL,
                                FECHA_NACIMIENTO DATE NOT NULL,
                                FECHA_CREACION TIMESTAMP DEFAULT NOW() NOT NULL,
                                FECHA_MODIFICACION TIMESTAMP DEFAULT NOW() NOT NULL,
                                CONSTRAINT CLIENTE_CHECK_TIPO_IDENTIFICACION CHECK((UPPER("TIPO_IDENTIFICACION") IN('CC', 'TC', 'CE', 'NIT'))),
                                CONSTRAINT "CLIENTE_CORREO_ELECTRONICO_UNIQUE" UNIQUE (CORREO_ELECTRONICO),
                                CONSTRAINT "CLIENTE_PK" PRIMARY KEY (ID_CLIENTE)
);

-- tabla producto financiero
CREATE TABLE public.producto_financiero (
                                            numero_producto numeric(38) NOT NULL,
                                            id_cliente int4 NOT NULL,
                                            tipo_producto varchar(2) NOT NULL,
                                            estado_producto varchar(1) DEFAULT 'A' NOT NULL,
                                            saldo float8 NOT NULL,
                                            execto_gmf bool DEFAULT false NOT NULL,
                                            fecha_creacion timestamp DEFAULT now() NOT NULL,
                                            fecha_modificacion timestamp DEFAULT now() NOT NULL,
                                            CONSTRAINT cuenta_pk PRIMARY KEY (numero_producto),
                                            CONSTRAINT producto_financiero_check_estado_producto CHECK (upper(estado_producto) in ('A', 'I', 'C')),
                                            CONSTRAINT producto_financiero_check_longitud_cuenta CHECK (length(numero_producto) = 10),
                                            CONSTRAINT producto_financiero_check_saldo_negativo CHECK (saldo >= 0),
                                            CONSTRAINT producto_financiero_check_tipo_cuenta CHECK (upper(tipo_producto) in ('CC', 'CA')),
                                            CONSTRAINT producto_financiero_cliente_fk FOREIGN KEY (id_cliente) REFERENCES public.cliente(id_cliente) ON DELETE RESTRICT ON UPDATE RESTRICT
);

-- tabla transaccion
CREATE TABLE public.transaccion (
                                    transaccion_id uuid NOT NULL,
                                    tipo_transaccion varchar(255) NOT NULL,
                                    cuenta_origen numeric(38) NOT NULL,
                                    cuenta_destino numeric(38) NULL,
                                    monto float8 NOT NULL,
                                    fecha_transaccion timestamp DEFAULT now() NOT NULL,
                                    CONSTRAINT transaccion_check_tipo CHECK (tipo_transaccion IN ('CONSIGNACION', 'RETIRO', 'TRANSFERENCIA')),
                                    CONSTRAINT transaccion_pk PRIMARY KEY (transaccion_id),
                                    CONSTRAINT transaccion_destino_producto_financiero_fk FOREIGN KEY (cuenta_destino) REFERENCES public.producto_financiero(numero_producto) ON DELETE RESTRICT ON UPDATE RESTRICT,
                                    CONSTRAINT transaccion_origen_producto_financiero_fk FOREIGN KEY (cuenta_origen) REFERENCES public.producto_financiero(numero_producto) ON DELETE RESTRICT ON UPDATE RESTRICT
);

