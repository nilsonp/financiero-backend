-- liquibase formatted sql

-- changeset nilsonp:1746493370247-1

-- Tabla cliente
CREATE TABLE public.cliente (
                                id_cliente serial NOT NULL,
                                tipo_identificacion varchar(5) NOT NULL,
                                numero_identificacion varchar(15) NOT NULL,
                                nombres varchar(50) NOT NULL,
                                apellidos varchar(50) NOT NULL,
                                correo_electronico varchar(50) NOT NULL,
                                fecha_nacimiento date NOT NULL,
                                fecha_creacion timestamp DEFAULT now() NOT NULL,
                                fecha_modificacion timestamp DEFAULT now() NOT NULL,
                                CONSTRAINT cliente_check_tipo_identificacion CHECK ((upper((tipo_identificacion)::text) = ANY (ARRAY['CC'::text, 'NIT'::text, 'TC'::text, 'CE'::text]))),
                                CONSTRAINT cliente_pk PRIMARY KEY (id_cliente),
                                CONSTRAINT correo_electronico_unique UNIQUE (correo_electronico),
                                CONSTRAINT tipo_numero_identificacion_unique UNIQUE (tipo_identificacion, numero_identificacion)
);

-- tabla producto financiero
CREATE TABLE public.producto_financiero (
                                            numero_producto numeric(38) NOT NULL,
                                            id_cliente int4 NOT NULL,
                                            tipo_producto varchar(2) NOT NULL,
                                            estado_producto varchar(1) DEFAULT 'A'::character varying NOT NULL,
                                            saldo float8 NOT NULL,
                                            execto_gmf bool DEFAULT false NOT NULL,
                                            fecha_creacion timestamp DEFAULT now() NOT NULL,
                                            fecha_modificacion timestamp DEFAULT now() NOT NULL,
                                            CONSTRAINT cuenta_pk PRIMARY KEY (numero_producto),
                                            CONSTRAINT producto_financiero_check_estado_producto CHECK ((upper((estado_producto)::text) = ANY (ARRAY['A'::text, 'I'::text, 'C'::text]))),
                                            CONSTRAINT producto_financiero_check_longitud_cuenta CHECK ((length(((numero_producto)::character varying)::text) = 10)),
                                            CONSTRAINT producto_financiero_check_saldo_negativo CHECK ((saldo >= (0)::double precision)),
                                            CONSTRAINT producto_financiero_check_tipo_cuenta CHECK ((upper((tipo_producto)::text) = ANY (ARRAY['CC'::text, 'CA'::text]))),
                                            CONSTRAINT producto_financiero_cliente_fk FOREIGN KEY (id_cliente) REFERENCES public.cliente(id_cliente) ON DELETE RESTRICT ON UPDATE RESTRICT
);

-- tabla transaccion
CREATE TABLE public.transaccion (
                                    transaccion_id uuid DEFAULT gen_random_uuid() NOT NULL,
                                    tipo_transaccion varchar(255) NOT NULL,
                                    cuenta_origen numeric(38) NOT NULL,
                                    cuenta_destino numeric(38) NULL,
                                    monto float8 NOT NULL,
                                    fecha_transaccion timestamp DEFAULT now() NOT NULL,
                                    CONSTRAINT transaccion_check_tipo CHECK (((tipo_transaccion)::text = ANY (ARRAY[('CONSIGNACION'::character varying)::text, ('RETIRO'::character varying)::text, ('TRASNFERENCIA'::character varying)::text]))),
	CONSTRAINT transaccion_pk PRIMARY KEY (transaccion_id),
	CONSTRAINT transaccion_destino_producto_financiero_fk FOREIGN KEY (cuenta_destino) REFERENCES public.producto_financiero(numero_producto) ON DELETE RESTRICT ON UPDATE RESTRICT,
	CONSTRAINT transaccion_origen_producto_financiero_fk FOREIGN KEY (cuenta_origen) REFERENCES public.producto_financiero(numero_producto) ON DELETE RESTRICT ON UPDATE RESTRICT
);

-- secuencias para generar numero de producto financiero
CREATE SEQUENCE public.producto_cuenta_ahorro_seq
    INCREMENT BY 1
    MINVALUE 3300000000
    MAXVALUE 9223372036854775807
    START 3300000000
	CACHE 1
	NO CYCLE;

CREATE SEQUENCE public.producto_cuenta_corriente_seq
    INCREMENT BY 1
    MINVALUE 5300000000
    MAXVALUE 9223372036854775807
    START 5300000000
	CACHE 1
	NO CYCLE;


