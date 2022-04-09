import sys;
import pandas as pd;
import numpy as np;
import matplotlib.pyplot as plt;
from datetime import datetime

caminhoSalvarImagens = sys.argv[1];
motor = sys.argv[2];
caminhoArquivo = sys.argv[3];
colunas = ['VAB', 'VBC', 'VCA', 'IA', 'IB', 'IC', 'TORQUE', 'NA', 'VELOCIDADE', 'TEMPO'];
datahora = datetime.now().strftime('%d_%m_%Y-%H_%M_%S');
nomeArquivo = motor.replace(' ', '') + '_' + datahora + '.png';
caminhoImagemSalva = caminhoSalvarImagens + nomeArquivo;

def lerArquivo():
    df = pd.read_csv(caminhoArquivo, sep = ",", header = None);
    df.columns = colunas;
    return df;

def plotarTensao(figTensao):
    figTensao.plot(df['TEMPO'].to_numpy(), df['VAB'].to_numpy(), color = 'red');
    figTensao.plot(df['TEMPO'].to_numpy(), df['VBC'].to_numpy(), color = 'green');
    figTensao.plot(df['TEMPO'].to_numpy(), df['VCA'].to_numpy(), color = 'blue');

    figTensao.set_title('TENSÃO');
    figTensao.set(xlabel = 'TEMPO', ylabel = 'TENSÃO (V)');
    figTensao.label_outer();

def plotarCorrente(figCorrente):
    figCorrente.plot(df['TEMPO'].to_numpy(), df['IA'].to_numpy(), color = 'red');
    figCorrente.plot(df['TEMPO'].to_numpy(), df['IB'].to_numpy(), color = 'green');
    figCorrente.plot(df['TEMPO'].to_numpy(), df['IC'].to_numpy(), color = 'blue');

    figCorrente.set_title('CORRENTE');
    figCorrente.set(xlabel = 'TEMPO', ylabel = 'CORRENTE (A)');
    figCorrente.label_outer();

df = lerArquivo();
df = df.iloc[-1500:];
fig, (figTensao, figCorrente) = plt.subplots(2)
plotarTensao(figTensao);
plotarCorrente(figCorrente);
fig.savefig(caminhoImagemSalva);

print('motor:%s' % motor);
print('arquivo:%s' % nomeArquivo);