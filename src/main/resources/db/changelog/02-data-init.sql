-- liquibase formatted sql

-- datos iniciales
INSERT INTO public.cliente
(tipo_identificacion, numero_identificacion, nombres, apellidos, correo_electronico, fecha_nacimiento)
VALUES('CC', '123456', 'Test', 'Test', 'test@test.com', '1980-12-01');

INSERT INTO public.producto_financiero
(numero_producto, id_cliente, tipo_producto, estado_producto, saldo, execto_gmf)
VALUES(5312312399, 1, 'CA', 'A', 1.0, false);
INSERT INTO public.producto_financiero
(numero_producto, id_cliente, tipo_producto, estado_producto, saldo, execto_gmf)
VALUES(3312312399, 1, 'CC', 'A', 0.0, false);

INSERT INTO public.transaccion
(tipo_transaccion, cuenta_origen, cuenta_destino, monto)
VALUES( 'CONSIGNACION', 5312312399, NULL, 1.0);
INSERT INTO public.transaccion
(tipo_transaccion, cuenta_origen, cuenta_destino, monto)
VALUES( 'CONSIGNACION', 3312312399, NULL, 0);

