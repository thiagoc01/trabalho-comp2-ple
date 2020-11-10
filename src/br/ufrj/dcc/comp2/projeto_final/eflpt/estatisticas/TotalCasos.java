package br.ufrj.dcc.comp2.projeto_final.eflpt.estatisticas;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import br.ufrj.dcc.comp2.projeto_final.eflpt.Medicao;

public class TotalCasos extends Estatistica {
	
	private Dados d = Dados.retornaInstancia();	
	private ArrayList<Medicao> numConfirmados = d.getConfirmados();
	private ArrayList<Medicao> numMortos = d.getMortos();
	private LocalDate dataInicio;
	private LocalDate dataFim;
	
	public String[] rankingCasos() {
		
		StringBuilder sb = new StringBuilder();
		String pais = null;
		int casos = 0;
		
		String[] ranking = null;
		
		TreeMap<Integer,String> rankingCasos = new TreeMap<>(Collections.reverseOrder());
		
		for(Medicao m : numConfirmados) {
			if(m.getMomento().toLocalDate().equals(dataFim)) {
				rankingCasos.put(m.getCasos(), m.getPais().getNome());
			}
		}
		for(Map.Entry<Integer,String> entry : rankingCasos.entrySet()) {
			pais = entry.getValue();
			casos = entry.getKey();
			sb.append(pais).append("!").append(casos).append(";");
		}
		ranking = sb.toString().split(";");
		
		
		return ranking;
		
	}
	
	public String[] rankingCasosCrescimento() {
		
		int casosIniciais = 0;
		int casosFinais = 0;
		int taxaCrescimento =  0;
		String[] ranking = null;

		
		StringBuilder sb = new StringBuilder();
		
		TreeMap<String,Integer> casosInicial = new TreeMap<>();
		TreeMap<String,Integer> casosFinal = new TreeMap<>();
		
		TreeMap<Integer,String> rankingCrescimento = new TreeMap<>(Collections.reverseOrder());
		
		for(Medicao m : numConfirmados) {
			if(m.getMomento().toLocalDate().equals(dataFim)) {
				casosFinal.put(m.getPais().getNome(), m.getCasos());
			}
			if(m.getMomento().toLocalDate().equals(dataInicio)) {
				casosInicial.put(m.getPais().getNome(), m.getCasos());
			}
		}
		
		for(Map.Entry<String,Integer> entry : casosInicial.entrySet()){
			
			for(Map.Entry<String, Integer> mep : casosFinal.entrySet()){
				
				if(entry.getKey().equals(mep.getKey())) {
					casosIniciais = 1;
					casosFinais = mep.getValue();
					if(casosIniciais == 0 && casosFinais != 0) {
						taxaCrescimento = casosFinais;
					
					}
					else if(casosFinais == 0) {
						taxaCrescimento = 0;
					}
					else{
						taxaCrescimento = ((casosFinais - casosIniciais)/casosIniciais)*100;
					}
					rankingCrescimento.put(taxaCrescimento, entry.getKey());
					
					}
				}
			
			}
		
		for(Map.Entry<Integer,String> entry : rankingCrescimento.entrySet()) {
			sb.append(entry.getKey()).append("%").append("!").append(entry.getValue()).append(";");
		}
		
		ranking = sb.toString().split(";");
		
		return ranking;
		
	}
	
	
	
	public String[] rankingRaio(int raio, String pais) {
		
		
		float latPaisKm = 0;
		float lonPaisKm = 0;
		float distanciaKm = 0;
		
		String [] ranking = null;
		StringBuilder sb = new StringBuilder();
		
		TreeMap<Double,String> rankingProximos = new TreeMap<>();
		
		for(Medicao m : numConfirmados) {
			if(m.getPais().getNome().equals(pais)) {
				latPaisKm = (m.getPais().getLatitude())*60*1.852f;
				lonPaisKm = (m.getPais().getLongitude())*60*1.852f;
			}
		}
		
		
		for(Medicao m : numConfirmados) {
			if(!(m.getPais().getNome().equals(pais))) {
				float latOutro = (m.getPais().getLatitude())*60*1.852f;
				float lonOutro = (m.getPais().getLongitude())*60*1.852f;
				distanciaKm = (float) Math.sqrt(Math.pow(latPaisKm-latOutro,2)+Math.pow(lonPaisKm-lonOutro,2));
				if(distanciaKm <= raio) {
					rankingProximos.put((double)distanciaKm, m.getPais().getNome());
				}
			}
		}
		
		for(Map.Entry<Double,String> entry : rankingProximos.entrySet()) {
			sb.append(entry.getValue()).append("!").append(entry.getKey()).append(";");
		}
		
		ranking = sb.toString().split(";");
		
		return ranking;
		
	}
			
	public String[] rankingMortalidade() {
		
		TreeMap<String,Integer> confirmados = new TreeMap<>();
		TreeMap<String,Integer> mortos = new TreeMap<>();
		TreeMap<Double,String> mortalidade = new TreeMap<>(Collections.reverseOrder());
		
		
		
		String[] ranking = null;
		Double morts = null;
		Double confim = null;
		Double mortali = null;
		StringBuilder sb = new StringBuilder();
		
		for(Medicao m : numConfirmados) {
			if(m.getMomento().toLocalDate().equals(dataFim)) {
				confirmados.put(m.getPais().getNome(), m.getCasos());
				}
			}
		
		for(Medicao m : numMortos) {
			if(m.getMomento().toLocalDate().equals(dataFim)) {
				mortos.put(m.getPais().getNome(), m.getCasos());
				}
			}
		
		for(Map.Entry<String,Integer> entry : confirmados.entrySet()){	
			for(Map.Entry<String, Integer> mep : mortos.entrySet()){
				if(entry.getKey().equals(mep.getKey())) {
						confim = (double)(entry.getValue());
						morts = (double)(mep.getValue());
						if(confim == 0 && morts == 0) {
							mortali = 0.0;
						}
						else {
							mortali = (morts/confim)*100;
						}
						mortalidade.put(mortali, entry.getKey());
						}
					}
				}
		
		for(Map.Entry<Double,String> entry : mortalidade.entrySet()) {
			sb.append(entry.getValue()).append("!").append(String.format("%.3f",entry.getKey())).append("%").append(";");
		}
		ranking = sb.toString().split(";");
		
		return ranking;
	}
	
	@Override
	public LocalDate dataInicio() {
		return dataInicio;
	}

	@Override
	public LocalDate dataFim() {
		return dataFim;
	}

	@Override
	public float valor() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public void setDataFim(LocalDate fim) {
			dataFim = fim;
	}
	
	public void setDataInicio(LocalDate inicio) {
		 dataInicio = inicio;
	}
}
