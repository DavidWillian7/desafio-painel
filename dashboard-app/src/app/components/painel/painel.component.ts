import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DashboardService } from '../../service/dashboard.service';
import { VaccinationData } from '../../models/VaccinationData';
import * as Plotly from 'plotly.js-dist-min';
import { Layout } from 'plotly.js';

@Component({
  selector: 'app-painel',
  standalone: true,
  imports: [CommonModule],
  providers: [DashboardService],
  templateUrl: './painel.component.html',
  styleUrls: ['./painel.component.css']
})
export class PainelComponent {
  graficosGerados = false;
  dadosCarregados = false;
  carregando = false;
  mostrarMensagemErro = false;

  constructor(private dashboardService: DashboardService) {}

  criarGraficos() {
    if (this.graficosGerados || this.carregando) return;

    this.carregando = true;
    this.mostrarMensagemErro = false;

    this.dashboardService.getCovidData().subscribe({
      next: (dados) => {
        if (dados && dados.length > 0) {
          this.dadosCarregados = true;
          this.graficosGerados = true;
          this.carregando = false;
          setTimeout(() => this.gerarGraficos(dados), 17000);
        } else {
          this.carregando = false;
          this.mostrarMensagemErro = true;
        }
      },
      error: (erro) => {
        console.error('Erro ao obter dados:', erro);
        this.carregando = false;
        this.mostrarMensagemErro = true;
      }
    });
  }

  gerarGraficos(dados: VaccinationData[]) {
    if (this.dadosCarregados && dados && dados.length > 0) {
      this.criarGraficoVacinacaoTotal(dados);
      this.criarGraficoVacinacaoDiaria(dados);
      this.criarGraficoVacinacaoPorCem(dados);
    } else {
      console.error('Dados não estão prontos ou estão vazios');
    }
  }

  criarGraficoVacinacaoTotal(dados: VaccinationData[]) {
    const paises = [...new Set(dados.map(d => d.country))];
    const traces: Plotly.Data[] = [];

    paises.forEach(pais => {
      const dadosPais = dados.filter(d => d.country === pais);
      traces.push({
        x: dadosPais.map(d => d.date),
        y: dadosPais.map(d => d.totalVaccinations),
        type: 'scatter',
        mode: 'lines',
        name: pais
      });
    });

    const layout: Partial<Layout> = {
      title: 'Total de Vacinações por País',
      xaxis: { 
        title: 'Data',
        tickangle: -45,
        tickfont: { size: 10 }  
      },
      yaxis: { title: 'Total de Vacinações' },
      autosize: true,
      legend: { orientation: 'h', y: -0.5 },  
      margin: { b: 200, l: 100, r: 50, t: 50 },  
      height: 600
    };

    const config: Partial<Plotly.Config> = {
      responsive: true,
      displayModeBar: false 
    };

    Plotly.newPlot('grafico1', traces, layout, config);
  }

  criarGraficoVacinacaoDiaria(dados: VaccinationData[]) {

    const paises = [...new Set(dados.map(d => d.country))];

    const traces: Plotly.Data[] = paises.map(pais => {
      const dadosPais = dados.filter(d => d.country === pais);

      return {
        x: dadosPais.map(d => d.date),
        y: dadosPais.map(d => d.dailyVaccinations),
        type: 'bar',
        name: pais
      };
    });

    const layout: Partial<Layout> = {
      title: 'Vacinações Diárias por País',
      xaxis: { 
        title: 'Data',
        tickangle: -45,
        tickfont: { size: 10 }  
      },
      yaxis: { title: 'Vacinações Diárias' },
      barmode: 'group' as const,
      autosize: true,
      legend: { orientation: 'h', y: -0.7 },  
      margin: { b: 200, l: 100, r: 50, t: 50 },  
      height: 600  
    };

    const config: Partial<Plotly.Config> = {
      responsive: true,
      displayModeBar: false
    };

    Plotly.newPlot('grafico2', traces, layout, config);
  }

  criarGraficoVacinacaoPorCem(dados: VaccinationData[]) {

    const ultimosDados = dados.reduce((acc, curr) => {
      if (!acc[curr.country] || new Date(curr.date) > new Date(acc[curr.country].date)) {
        acc[curr.country] = curr;
      }
      return acc;
    }, {} as { [key: string]: VaccinationData });

    const paises = Object.keys(ultimosDados);
    const valores = paises.map(pais => ultimosDados[pais].totalVaccinationsPerHundred);

    const data: Plotly.Data[] = [{
      x: paises,
      y: valores,
      type: 'bar',
      marker: {
        color: 'rgba(0,123,255,0.7)',
        line: {
          color: 'rgba(0,123,255,1)',
          width: 1
        }
      }
    }];

    const layout: Partial<Layout> = {
      title: 'Total de Vacinações por 100 Pessoas (Último Dado)',
      xaxis: { 
        title: 'País',
        tickangle: -45  
      },
      yaxis: { title: 'Vacinações por 100 Pessoas' },
      autosize: true,
      margin: { b: 150 } 
    };

    const config: Partial<Plotly.Config> = {
      responsive: true,
      displayModeBar: false
    };

    Plotly.newPlot('grafico3', data, layout, config);
  }
}
