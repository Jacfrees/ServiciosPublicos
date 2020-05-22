package com.sigiep.serviciospublicos.models;

public class LecturaEntity {

    String identificador;
    String fecha;
    String fecha_vencimiento;
    String periodo;
    String numero_factura;
    String sector;
    String codigo_ruta;
    String codigo_interno;
    String usuario;
    String direccion;
    String estrato;
    String uso;
    String mes_6;
    String mes_5;
    String mes_4;
    String mes_3;
    String mes_2;
    String mes_1;
    String promedio;
    String lectura_anterior;
    String lectura_actual;
    String lectura;
    String consumo_basico;
    String deuda_anterior;
    String atraso;
    String estado_medidor;
    String casa_vacia;
    String valor_mtr3_acueducto;
    String cargo_fijo_acueducto;
    String consumo_acueducto;
    String contribucion_acueducto;
    String intereses_mora_de_acueducto;
    String subsidio_acueducto;
    String acueducto_covid19;
    String valor_mtr3_alcantarillado;
    String cargo_fijo_alcantarillado;
    String consumo_alcantarillado;
    String contribucion_alcantarillado;
    String intereses_mora_de_alcantarillado;
    String subsidio_alcantarillado;
    String alcantarillado_covid19;
    String valor_mtr3_aseo;
    String cargo_fijo_aseo;
    String subsidio_aseo;
    String intereses_de_mora_aseo;
    String contribucion_aseo;

    public LecturaEntity(String identificador, String fecha, String fecha_vencimiento, String periodo, String numero_factura,
                         String sector, String codigo_ruta, String codigo_interno, String usuario, String direccion,
                         String estrato, String uso, String mes_6, String mes_5, String mes_4, String mes_3, String mes_2,
                         String mes_1, String promedio, String lectura_anterior, String lectura_actual, String lectura,
                         String consumo_basico, String deuda_anterior, String atraso, String estado_medidor,
                         String casa_vacia, String valor_mtr3_acueducto, String cargo_fijo_acueducto,
                         String consumo_acueducto, String contribucion_acueducto, String intereses_mora_de_acueducto,
                         String subsidio_acueducto, String acueducto_covid19, String valor_mtr3_alcantarillado,
                         String cargo_fijo_alcantarillado, String consumo_alcantarillado, String contribucion_alcantarillado,
                         String intereses_mora_de_alcantarillado, String subsidio_alcantarillado, String alcantarillado_covid19,
                         String valor_mtr3_aseo, String cargo_fijo_aseo, String subsidio_aseo, String intereses_de_mora_aseo,
                         String contribucion_aseo) {
        this.identificador = identificador;
        this.fecha = fecha;
        this.fecha_vencimiento = fecha_vencimiento;
        this.periodo = periodo;
        this.numero_factura = numero_factura;
        this.sector = sector;
        this.codigo_ruta = codigo_ruta;
        this.codigo_interno = codigo_interno;
        this.usuario = usuario;
        this.direccion = direccion;
        this.estrato = estrato;
        this.uso = uso;
        this.mes_6 = mes_6;
        this.mes_5 = mes_5;
        this.mes_4 = mes_4;
        this.mes_3 = mes_3;
        this.mes_2 = mes_2;
        this.mes_1 = mes_1;
        this.promedio = promedio;
        this.lectura_anterior = lectura_anterior;
        this.lectura_actual = lectura_actual;
        this.lectura = lectura;
        this.consumo_basico = consumo_basico;
        this.deuda_anterior = deuda_anterior;
        this.atraso = atraso;
        this.estado_medidor = estado_medidor;
        this.casa_vacia = casa_vacia;
        this.valor_mtr3_acueducto = valor_mtr3_acueducto;
        this.cargo_fijo_acueducto = cargo_fijo_acueducto;
        this.consumo_acueducto = consumo_acueducto;
        this.contribucion_acueducto = contribucion_acueducto;
        this.intereses_mora_de_acueducto = intereses_mora_de_acueducto;
        this.subsidio_acueducto = subsidio_acueducto;
        this.acueducto_covid19 = acueducto_covid19;
        this.valor_mtr3_alcantarillado = valor_mtr3_alcantarillado;
        this.cargo_fijo_alcantarillado = cargo_fijo_alcantarillado;
        this.consumo_alcantarillado = consumo_alcantarillado;
        this.contribucion_alcantarillado = contribucion_alcantarillado;
        this.intereses_mora_de_alcantarillado = intereses_mora_de_alcantarillado;
        this.subsidio_alcantarillado = subsidio_alcantarillado;
        this.alcantarillado_covid19 = alcantarillado_covid19;
        this.valor_mtr3_aseo = valor_mtr3_aseo;
        this.cargo_fijo_aseo = cargo_fijo_aseo;
        this.subsidio_aseo = subsidio_aseo;
        this.intereses_de_mora_aseo = intereses_de_mora_aseo;
        this.contribucion_aseo = contribucion_aseo;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getFecha_vencimiento() {
        return fecha_vencimiento;
    }

    public void setFecha_vencimiento(String fecha_vencimiento) {
        this.fecha_vencimiento = fecha_vencimiento;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getNumero_factura() {
        return numero_factura;
    }

    public void setNumero_factura(String numero_factura) {
        this.numero_factura = numero_factura;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getCodigo_ruta() {
        return codigo_ruta;
    }

    public void setCodigo_ruta(String codigo_ruta) {
        this.codigo_ruta = codigo_ruta;
    }

    public String getCodigo_interno() {
        return codigo_interno;
    }

    public void setCodigo_interno(String codigo_interno) {
        this.codigo_interno = codigo_interno;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEstrato() {
        return estrato;
    }

    public void setEstrato(String estrato) {
        this.estrato = estrato;
    }

    public String getUso() {
        return uso;
    }

    public void setUso(String uso) {
        this.uso = uso;
    }

    public String getMes_6() {
        return mes_6;
    }

    public void setMes_6(String mes_6) {
        this.mes_6 = mes_6;
    }

    public String getMes_5() {
        return mes_5;
    }

    public void setMes_5(String mes_5) {
        this.mes_5 = mes_5;
    }

    public String getMes_4() {
        return mes_4;
    }

    public void setMes_4(String mes_4) {
        this.mes_4 = mes_4;
    }

    public String getMes_3() {
        return mes_3;
    }

    public void setMes_3(String mes_3) {
        this.mes_3 = mes_3;
    }

    public String getMes_2() {
        return mes_2;
    }

    public void setMes_2(String mes_2) {
        this.mes_2 = mes_2;
    }

    public String getMes_1() {
        return mes_1;
    }

    public void setMes_1(String mes_1) {
        this.mes_1 = mes_1;
    }

    public String getPromedio() {
        return promedio;
    }

    public void setPromedio(String promedio) {
        this.promedio = promedio;
    }

    public String getLectura_anterior() {
        return lectura_anterior;
    }

    public void setLectura_anterior(String lectura_anterior) {
        this.lectura_anterior = lectura_anterior;
    }

    public String getLectura_actual() {
        return lectura_actual;
    }

    public void setLectura_actual(String lectura_actual) {
        this.lectura_actual = lectura_actual;
    }

    public String getLectura() {
        return lectura;
    }

    public void setLectura(String lectura) {
        this.lectura = lectura;
    }

    public String getConsumo_basico() {
        return consumo_basico;
    }

    public void setConsumo_basico(String consumo_basico) {
        this.consumo_basico = consumo_basico;
    }

    public String getDeuda_anterior() {
        return deuda_anterior;
    }

    public void setDeuda_anterior(String deuda_anterior) {
        this.deuda_anterior = deuda_anterior;
    }

    public String getAtraso() {
        return atraso;
    }

    public void setAtraso(String atraso) {
        this.atraso = atraso;
    }

    public String getEstado_medidor() {
        return estado_medidor;
    }

    public void setEstado_medidor(String estado_medidor) {
        this.estado_medidor = estado_medidor;
    }

    public String getCasa_vacia() {
        return casa_vacia;
    }

    public void setCasa_vacia(String casa_vacia) {
        this.casa_vacia = casa_vacia;
    }

    public String getValor_mtr3_acueducto() {
        return valor_mtr3_acueducto;
    }

    public void setValor_mtr3_acueducto(String valor_mtr3_acueducto) {
        this.valor_mtr3_acueducto = valor_mtr3_acueducto;
    }

    public String getCargo_fijo_acueducto() {
        return cargo_fijo_acueducto;
    }

    public void setCargo_fijo_acueducto(String cargo_fijo_acueducto) {
        this.cargo_fijo_acueducto = cargo_fijo_acueducto;
    }

    public String getConsumo_acueducto() {
        return consumo_acueducto;
    }

    public void setConsumo_acueducto(String consumo_acueducto) {
        this.consumo_acueducto = consumo_acueducto;
    }

    public String getContribucion_acueducto() {
        return contribucion_acueducto;
    }

    public void setContribucion_acueducto(String contribucion_acueducto) {
        this.contribucion_acueducto = contribucion_acueducto;
    }

    public String getIntereses_mora_de_acueducto() {
        return intereses_mora_de_acueducto;
    }

    public void setIntereses_mora_de_acueducto(String intereses_mora_de_acueducto) {
        this.intereses_mora_de_acueducto = intereses_mora_de_acueducto;
    }

    public String getSubsidio_acueducto() {
        return subsidio_acueducto;
    }

    public void setSubsidio_acueducto(String subsidio_acueducto) {
        this.subsidio_acueducto = subsidio_acueducto;
    }

    public String getAcueducto_covid19() {
        return acueducto_covid19;
    }

    public void setAcueducto_covid19(String acueducto_covid19) {
        this.acueducto_covid19 = acueducto_covid19;
    }

    public String getValor_mtr3_alcantarillado() {
        return valor_mtr3_alcantarillado;
    }

    public void setValor_mtr3_alcantarillado(String valor_mtr3_alcantarillado) {
        this.valor_mtr3_alcantarillado = valor_mtr3_alcantarillado;
    }

    public String getCargo_fijo_alcantarillado() {
        return cargo_fijo_alcantarillado;
    }

    public void setCargo_fijo_alcantarillado(String cargo_fijo_alcantarillado) {
        this.cargo_fijo_alcantarillado = cargo_fijo_alcantarillado;
    }

    public String getConsumo_alcantarillado() {
        return consumo_alcantarillado;
    }

    public void setConsumo_alcantarillado(String consumo_alcantarillado) {
        this.consumo_alcantarillado = consumo_alcantarillado;
    }

    public String getContribucion_alcantarillado() {
        return contribucion_alcantarillado;
    }

    public void setContribucion_alcantarillado(String contribucion_alcantarillado) {
        this.contribucion_alcantarillado = contribucion_alcantarillado;
    }

    public String getIntereses_mora_de_alcantarillado() {
        return intereses_mora_de_alcantarillado;
    }

    public void setIntereses_mora_de_alcantarillado(String intereses_mora_de_alcantarillado) {
        this.intereses_mora_de_alcantarillado = intereses_mora_de_alcantarillado;
    }

    public String getSubsidio_alcantarillado() {
        return subsidio_alcantarillado;
    }

    public void setSubsidio_alcantarillado(String subsidio_alcantarillado) {
        this.subsidio_alcantarillado = subsidio_alcantarillado;
    }

    public String getAlcantarillado_covid19() {
        return alcantarillado_covid19;
    }

    public void setAlcantarillado_covid19(String alcantarillado_covid19) {
        this.alcantarillado_covid19 = alcantarillado_covid19;
    }

    public String getValor_mtr3_aseo() {
        return valor_mtr3_aseo;
    }

    public void setValor_mtr3_aseo(String valor_mtr3_aseo) {
        this.valor_mtr3_aseo = valor_mtr3_aseo;
    }

    public String getCargo_fijo_aseo() {
        return cargo_fijo_aseo;
    }

    public void setCargo_fijo_aseo(String cargo_fijo_aseo) {
        this.cargo_fijo_aseo = cargo_fijo_aseo;
    }

    public String getSubsidio_aseo() {
        return subsidio_aseo;
    }

    public void setSubsidio_aseo(String subsidio_aseo) {
        this.subsidio_aseo = subsidio_aseo;
    }

    public String getIntereses_de_mora_aseo() {
        return intereses_de_mora_aseo;
    }

    public void setIntereses_de_mora_aseo(String intereses_de_mora_aseo) {
        this.intereses_de_mora_aseo = intereses_de_mora_aseo;
    }

    public String getContribucion_aseo() {
        return contribucion_aseo;
    }

    public void setContribucion_aseo(String contribucion_aseo) {
        this.contribucion_aseo = contribucion_aseo;
    }
}
