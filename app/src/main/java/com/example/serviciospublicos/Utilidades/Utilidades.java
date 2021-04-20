package com.example.serviciospublicos.Utilidades;

public class Utilidades {

        public static final String table_usuario = "create table usuario(id_unico integer PRIMARY KEY, usuario text, contrasen text, fechaactualizacion text, observaciones text, rol INTEGER, tercero INTEGER, estado INTEGER)";
        public static final String table_compania = "create table compania(id_compania integer PRIMARY KEY AUTOINCREMENT, nombre text DEFAULT NULL, nit text DEFAULT NULL, correo text DEFAULT NULL, direccion text DEFAULT NULL, telefono text DEFAULT NULL, slogan text DEFAULT NULL, ruta_logo text DEFAULT NULL, ciudad text DEFAULT NULL, codigo_ean text DEFAULT NULL, nuir text DEFAULT NULL)";
        public static final String table_eventualidad = "create table eventualidad(id_unico integer PRIMARY KEY, nombre text, tipo_eventualidad integer, valor integer)";
        public static final String table_lectura = "create table lectura(" +
                "id_unico integer PRIMARY KEY, " +
                "identificador integer DEFAULT NULL, " +
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
                "promedio integer DEFAULT NULL, " +
                "consumo_basico integer DEFAULT NULL, " +
                "consumo_complementario integer DEFAULT NULL, " +
                "consumo_suntuario integer DEFAULT NULL, " +
                "deuda_anterior double DEFAULT NULL, " +
                "atraso integer DEFAULT NULL, " +
                "estado_medidor integer DEFAULT NULL, " +
                "eventualidad text DEFAULT NULL, " +
                "lectura_anterior integer DEFAULT NULL, " +
                "lectura_actual text DEFAULT NULL, " +
                "consumo integer DEFAULT NULL, " +
                "consumo_facturado integer DEFAULT NULL, " +
                "acueducto_valor_mtr_3 double DEFAULT NULL, " +
                "acueducto_cargo_fijo double DEFAULT NULL, " +
                "acueducto_consumo_basico text DEFAULT NULL, " +
                "acueducto_consumo_complementario text DEFAULT NULL, " +
                "acueducto_consumo_suntuario text DEFAULT NULL, " +
                "acueducto_subsido_cargo_fijo text DEFAULT NULL, " +
                "acueducto_subsido_basico text DEFAULT NULL, " +
                "acueducto_subsido_complementario text DEFAULT NULL, " +
                "acueducto_subsido_suntuario text DEFAULT NULL, " +
                "acueducto_contribucion text DEFAULT NULL, " +
                "acueducto_mora text DEFAULT NULL, " +
                "acueducto_concepto1 text DEFAULT NULL, " +
                "acueducto_concepto2 text DEFAULT NULL, " +
                "acueducto_concepto3 text DEFAULT NULL, " +
                "alcantarillado_valor_mtr_3 text DEFAULT NULL, " +
                "alcantarillado_cargo_fijo text DEFAULT NULL, " +
                "alcantarillado_consumo_basico text DEFAULT NULL, " +
                "alcantarillado_consumo_complementario text DEFAULT NULL, " +
                "alcantarillado_consumo_suntuario text DEFAULT NULL, " +
                "alcantarillado_subsido_cargo_fijo text DEFAULT NULL, " +
                "alcantarillado_subsido_basico text DEFAULT NULL, " +
                "alcantarillado_subsido_complementario text DEFAULT NULL, " +
                "alcantarillado_subsido_suntuario text DEFAULT NULL, " +
                "alcantarillado_contribucion text DEFAULT NULL, " +
                "alcantarillado_mora text DEFAULT NULL, " +
                "alcantarillado_concepto1 text DEFAULT NULL, " +
                "alcantarillado_concepto2 text DEFAULT NULL, " +
                "alcantarillado_concepto3 text DEFAULT NULL, " +

                "aseo_valor_mtr_3 text DEFAULT NULL, " +
                "aseo_cargo_fijo text DEFAULT NULL, " +
                "aseo_consumo_basico text DEFAULT NULL, " +
                "aseo_consumo_complementario text DEFAULT NULL, " +
                "aseo_consumo_suntuario text DEFAULT NULL, " +
                "aseo_subsido_cargo_fijo text DEFAULT NULL, " +
                "aseo_subsido_basico text DEFAULT NULL, " +
                "aseo_subsido_complementario text DEFAULT NULL, " +
                "aseo_subsido_suntuario text DEFAULT NULL, " +
                "aseo_contribucion text DEFAULT NULL, " +
                "aseo_mora text DEFAULT NULL, " +
                "aseo_concepto1 text DEFAULT NULL, " +
                "aseo_concepto2 text DEFAULT NULL, " +
                "aseo_concepto3 text DEFAULT NULL, " +
                "matricula double DEFAULT NULL, " +
                "medidor double DEFAULT NULL, " +
                "llaves double DEFAULT NULL, " +
                "tapas double DEFAULT NULL, " +
                "financiacion text DEFAULT NULL, " +
                "reconexion double DEFAULT NULL, " +
                "fecha_factura text DEFAULT NULL, " +
                "aforador text DEFAULT NULL, "+
                "observaciones text DEFAULT NULL, " +
                "mora text DEFAULT NULL, " +
                "tipo_cliente text DEFAULT NULL, " +
                "cft text DEFAULT NULL, " +
                "trbl text DEFAULT NULL, " +
                "historico_123 text DEFAULT NULL, " +
                "historico_456 text DEFAULT NULL, " +
                "aseo text DEFAULT NULL, "+
                "subsidio text DEFAULT NULL, " +
                "deuda text DEFAULT NULL, " +
                "ajuste_d text DEFAULT NULL, " +
                "ajuste_c text DEFAULT NULL, " +
                "uni_residenciales text DEFAULT NULL, " +
                "un_comerciales text DEFAULT NULL, " +
                "porcentaje_subsidio text DEFAULT NULL, " +
                "porcentaje_contribucion text DEFAULT NULL, " +
                "frec_barrido text DEFAULT NULL, " +
                "frec_recoleccion text DEFAULT NULL, " +
                "total_aseo text DEFAULT NULL, " +
                "observaciones_financiacion text DEFAULT NULL, " +
                "ajuste_peso double DEFAULT NULL, " +
                "contribucion_aseo text DEFAULT NULL, " +
                "trna_tafna_tra_tafa text DEFAULT NULL, " +
                "abonos double DEFAULT NULL, " +
                "porcentaje double DEFAULT NULL, " +
                "fecha_suspension text DEFAULT NULL, " +
                "total_acueducto double DEFAULT NULL, " +
                "total_alcantarillado double DEFAULT NULL, " +
                "total_acdto_alc double DEFAULT NULL, " +
                "Sincronizo boolean DEFAULT NULL, " +
                "orden Integer DEFAULT NULL, " +
                "suspension double DEFAULT NULL, " +
                "geofono double DEFAULT NULL, " +
                "otros_cobros double DEFAULT NULL, " +
                "observaciones_otros_cobros text DEFAULT NULL, " +
                "conceptos_otros_cobros text DEFAULT NULL)";
        //"CHARACTER SET utf8mb4 COLLATE utf8mb4_bin";
    }



