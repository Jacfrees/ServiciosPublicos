package com.example.serviciospublicos.modelo;

import org.json.JSONObject;

public class Parse {

    public static ClFactura jsonToclFactura(JSONObject jsonObject){
        ClFactura clFactura = new ClFactura();
        clFactura.id_unico = jsonObject.optInt("id_unico");
        clFactura.identificador = jsonObject.optInt("identificador");
        clFactura.fecha = jsonObject.optString("fecha");
        clFactura.fecha_vencimiento = jsonObject.optString("fecha_vencimiento");
        clFactura.periodo = jsonObject.optString("periodo");
        clFactura.numero_factura = jsonObject.optString("numero_factura");
        clFactura.sector = jsonObject.optString("sector");
        clFactura.codigo_ruta = jsonObject.optString("codigo_ruta");
        clFactura.codigo_interno = jsonObject.optString("codigo_interno");
        clFactura.usuario = jsonObject.optString("usuario");
        clFactura.direccion = jsonObject.optString("direccion");
        clFactura.estrato = jsonObject.optString("estrato");
        clFactura.uso = jsonObject.optString("uso");
        clFactura.numero_medidor = jsonObject.optString("numero_medidor");
        clFactura.consumo_mes_6 = jsonObject.optInt("consumo_mes_6");
        clFactura.consumo_mes_5 = jsonObject.optInt("consumo_mes_5");
        clFactura.consumo_mes_4 = jsonObject.optInt("consumo_mes_4");
        clFactura.consumo_mes_3 = jsonObject.optInt("consumo_mes_3");
        clFactura.consumo_mes_2 = jsonObject.optInt("consumo_mes_2");
        clFactura.consumo_mes_1 = jsonObject.optInt("consumo_mes_1");
        clFactura.promedio = jsonObject.optInt("promedio");
        clFactura.consumo_basico = jsonObject.optInt("consumo_basico");
        clFactura.consumo_complementario = jsonObject.optString("consumo_complementario");
        clFactura.consumo_suntuario = jsonObject.optInt("consumo_suntuario");
        clFactura.deuda_anterior = jsonObject.optDouble("deuda_anterior");

        clFactura.atraso = jsonObject.optInt("atraso");
        clFactura.estado_medidor = jsonObject.optInt("estado_medidor");
        clFactura.eventualidad = jsonObject.isNull("eventualidad")?null:jsonObject.optString("eventualidad");
        clFactura.lectura_anterior = jsonObject.optInt("lectura_anterior");

        clFactura.lectura_actual = jsonObject.isNull("lectura_actual")?null:jsonObject.optString("lectura_actual");

        clFactura.consumo = jsonObject.optInt("consumo");
        clFactura.consumo_facturado = jsonObject.optDouble("consumo_facturado");

        clFactura.acueducto_valor_mtr_3 = jsonObject.optString("acueducto_valor_mtr_3");
        clFactura.acueducto_cargo_fijo = jsonObject.optString("acueducto_cargo_fijo");
        clFactura.acueducto_consumo_basico = jsonObject.optString("acueducto_consumo_basico");
        clFactura.acueducto_consumo_complementario = jsonObject.optString("acueducto_consumo_complementario");
        clFactura.acueducto_consumo_suntuario = jsonObject.optString("acueducto_consumo_suntuario");
        clFactura.acueducto_subsidio_cargo_fijo = jsonObject.optString("acueducto_subsido_cargo_fijo");
        clFactura.acueducto_subsidio_complementario = jsonObject.optString("acueducto_subsido_complementario");
        clFactura.acueducto_subsidio_suntuario = jsonObject.optString("acueducto_subsido_suntuario");
        clFactura.acueducto_contribucion = jsonObject.optString("acueducto_contribucion");
        clFactura.acueducto_mora = jsonObject.optString("acueducto_mora");
        clFactura.acueducto_concepto1 = jsonObject.optString("acueducto_concepto1");
        clFactura.acueducto_concepto2 = jsonObject.optString("acueducto_concepto2");
        clFactura.acueducto_concepto3 = jsonObject.optString("acueducto_concepto3");

        clFactura.alcantarillado_valor_mtr3 = jsonObject.optString("alcantarillado_valor_mtr_3");
        clFactura.alcantarillado_cargo_fijo = jsonObject.optString("alcantarillado_cargo_fijo");
        clFactura.alcantarillado_consumo_basico = jsonObject.optString("alcantarillado_consumo_basico");
        clFactura.alcantarilado__consumo_complementario = jsonObject.optString("alcantarillado_consumo_complementario");
        clFactura.alcantarillado_consumo_suntuario = jsonObject.optString("alcantarillado_consumo_suntuario");
        clFactura.alcantarillado_subsidio_cargo_fijo = jsonObject.optString("alcantarillado_subsido_cargo_fijo");
        clFactura.alcantarillado_subsidio_basico = jsonObject.optString("alcantarillado_subsido_basico");
        clFactura.alcantarillado_subsidio_complementario = jsonObject.optString("alcantarillado_subsido_complementario");
        clFactura.alcantarillado_subsidio_suntuario = jsonObject.optString("alcantarillado_subsido_suntuario");
        clFactura.alcantarillado_contribucion = jsonObject.optString("alcantarillado_contribucion");
        clFactura.alcantarillado_mora = jsonObject.optString("alcantarillado_mora");
        clFactura.alcantarillado_concepto1 = jsonObject.optString("alcantarillado_concepto1");
        clFactura.alcantarillado_concepto2 = jsonObject.optString("alcantarillado_concepto2");
        clFactura.alcantarillado_concepto3 = jsonObject.optString("alcantarillado_concepto3");

        clFactura.aseo_valor_mtr_3 = jsonObject.optString("aseo_valor_mtr_3");
        clFactura.aseo_cargo_fijo = jsonObject.optString("aseo_cargo_fijo");
        clFactura.aseo_consumo_basico = jsonObject.optString("aseo_consumo_basico");
        clFactura.aseo_consumo_complementario = jsonObject.optString("aseo_consumo_complementario");
        clFactura.aseo_consumo_suntuario = jsonObject.optString("aseo_consumo_sunturio");
        clFactura.aseo_subsidio_cargo_fijo = jsonObject.optString("aseo_subsido_cargo_fijo");
        clFactura.aseo_subsidio_basico = jsonObject.optString("aseo_subsido_basico");
        clFactura.aseo_subsidio_complementario = jsonObject.optString("aseo_subsido_complementario");
        clFactura.aseo_subsidio_suntuario = jsonObject.optString("aseo_subsido_suntuario");
        clFactura.aseo_contribucion = jsonObject.optString("aseo_contribucion");
        clFactura.aseo_mora = jsonObject.optString("aseo_mora");
        clFactura.aseo_concepto1 = jsonObject.optString("aseo_concepto1");
        clFactura.aseo_concepto2 = jsonObject.optString("aseo_concepto2");
        clFactura.aseo_concepto3 = jsonObject.optString("aseo_concepto3");

        clFactura.matricula = jsonObject.optDouble("matricula");
        clFactura.medidor = jsonObject.optDouble("medidor");
        clFactura.llaves = jsonObject.optDouble("llaves");
        clFactura.tapas = jsonObject.optDouble("tapas");
        clFactura.financiacion = jsonObject.optDouble("financiacion");
        clFactura.reconexion = jsonObject.optDouble("reconexion");
        clFactura.fecha_factura = jsonObject.optString("fecha_factura");
        clFactura.aforador = jsonObject.optString("aforador");
        clFactura.observaciones = jsonObject.optString("observaciones");
        clFactura.mora = jsonObject.optString("mora");
        clFactura.tipo_cliente = jsonObject.optString("tipo_cliente");
        clFactura.cft = jsonObject.optString("cft");
        clFactura.trbl = jsonObject.optString("trbl");
        clFactura.historico_123 = jsonObject.optString("historico_123");
        clFactura.historico_456 = jsonObject.optString("historico_456");
        clFactura.aseo = jsonObject.optString("aseo");
        clFactura.subsidio = jsonObject.optString("subsidio");
        clFactura.deuda = jsonObject.optString("deuda");
        clFactura.ajuste_d = jsonObject.optString("ajuste_d");
        clFactura.ajuste_c = jsonObject.optString("ajuste_c");
        clFactura.uni_residenciales = jsonObject.optString("uni_residenciales");
        clFactura.un_comerciales = jsonObject.optString("un_comerciales");
        clFactura.porcentaje_subsidio = jsonObject.optString("porcentaje_subsidio");
        clFactura.porcentaje_contribucion = jsonObject.optString("porcentaje_contribucion");
        clFactura.frec_barrido = jsonObject.optString("frec_barrido");
        clFactura.frec_recoleccion =jsonObject.optString("frec_recoleccion");
        clFactura.total_aseo = jsonObject.optString("total_aseo");
        clFactura.observaciones_financiacion = jsonObject.optString("observaciones_financiacion");
        clFactura.ajuste_peso = jsonObject.optDouble("ajuste_peso");

        return clFactura;
    }

}
