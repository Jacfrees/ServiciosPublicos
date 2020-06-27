package com.sigiep.serviciospublicos.utilities;

public class Utilidades {

    public static final String table_usuario = "create table usuario(id_usuario integer PRIMARY KEY AUTOINCREMENT, nombre text, documento text, usuario text, contrasena text)";
    public static final String table_compania = "create table compania(id_compania integer PRIMARY KEY AUTOINCREMENT, nombre text DEFAULT NULL, nit text DEFAULT NULL, correo text DEFAULT NULL, direccion text DEFAULT NULL, telefono text DEFAULT NULL, slogan text DEFAULT NULL, ruta_logo text DEFAULT NULL, ciudad text DEFAULT NULL, codigo_ean text DEFAULT NULL, nuir text DEFAULT NULL)";
    public static final String table_lectura = "create table lectura(" +
            "identificador text DEFAULT NULL, " +
            "fecha text DEFAULT NULL, " +
            "fecha_vencimiento text DEFAULT NULL, " +
            "periodo text DEFAULT NULL, " +
            "numero_factura text DEFAULT NULL, " +
            "sector text DEFAULT NULL, " +
            "codigo_ruta text DEFAULT NULL, " +
            "codigo_interno text DEFAULT NULL, " +
            "usuario text DEFAULT NULL, " +
            "direccion text DEFAULT NULL, " +
            "estrato text DEFAULT NULL, " +
            "uso text DEFAULT NULL, " +
            "numero_medidor text DEFAULT NULL, " +
            "consumo_mes_6 text DEFAULT NULL, " +
            "consumo_mes_5 text DEFAULT NULL, " +
            "consumo_mes_4 text DEFAULT NULL, " +
            "consumo_mes_3 text DEFAULT NULL, " +
            "consumo_mes_2 text DEFAULT NULL, " +
            "consumo_mes_1 text DEFAULT NULL, " +
            "promedio text DEFAULT NULL, " +

            "consumo_basico text DEFAULT NULL, " +
            "mtrs_max_subsidio text DEFAULT NULL, " +
            "deuda_anterior text DEFAULT NULL, " +
            "atraso text DEFAULT NULL, " +
            "estado_medidor text DEFAULT NULL, " +
            "casa_vacia text DEFAULT NULL, " +
            "lectura_anterior text DEFAULT NULL, " +
            "lectura_actual text DEFAULT NULL, " +
            "lectura text DEFAULT NULL, " +

            "valor_mtr3_acueducto text DEFAULT NULL, " +
            "cargo_fijo_acueducto text DEFAULT NULL, " +
            "consumo_acueducto text DEFAULT NULL, " +
            "contribucion_acueducto text DEFAULT NULL, " +
            "intereses_mora_de_acueducto text DEFAULT NULL, " +
            "subsidio_acueducto text DEFAULT NULL, " +
            "acueducto_concepto1 text DEFAULT NULL, " +
            "acueducto_concepto2 text DEFAULT NULL, " +
            "acueducto_concepto3 text DEFAULT NULL, " +

            "valor_mtr3_alcantarillado text DEFAULT NULL, " +
            "cargo_fijo_alcantarillado text DEFAULT NULL, " +
            "consumo_alcantarillado text DEFAULT NULL, " +
            "contribucion_alcantarillado text DEFAULT NULL, " +
            "intereses_mora_de_alcantarillado text DEFAULT NULL, " +
            "subsidio_alcantarillado text DEFAULT NULL, " +
            "alcantarillado_concepto1 text DEFAULT NULL, " +
            "alcantarillado_concepto2 text DEFAULT NULL, " +
            "alcantarillado_concepto3 text DEFAULT NULL, " +

            "valor_mtr3_aseo text DEFAULT NULL, " +
            "cargo_fijo_aseo text DEFAULT NULL, " +
            "subsidio_aseo text DEFAULT NULL, " +
            "intereses_de_mora_aseo text DEFAULT NULL, " +
            "contribucion_aseo text DEFAULT NULL, " +
            "aseo_concepto1 text DEFAULT NULL, " +
            "aseo_concepto2 text DEFAULT NULL, " +
            "aseo_concepto3 text DEFAULT NULL, " +

            "matricula text DEFAULT NULL, " +
            "medidor text DEFAULT NULL, " +
            "llave_o_tapas text DEFAULT NULL, " +
            "financiacion text DEFAULT NULL, " +
            "reconexion text DEFAULT NULL, " +
            "fecha_lectura text DEFAULT NULL, " +
            "aforador text DEFAULT NULL, " +
            "servicio_acueducto text DEFAULT NULL, " +
            "servicio_alcantarillado text DEFAULT NULL, " +
            "servicio_aseo text DEFAULT NULL, "+
            "porcentaje_subsidio_acueducto text DEFAULT NULL, " +
            "porcentaje_subsidio_alcantarillado text DEFAULT NULL)";



    //"CHARACTER SET utf8mb4 COLLATE utf8mb4_bin";
}
