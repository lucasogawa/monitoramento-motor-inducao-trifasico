import sys;
import pandas as pd;
import numpy as np;

id = sys.argv[1];
motor = sys.argv[2];
caminhoArquivo = sys.argv[3];
colunas = ['VAB', 'VBC', 'VCA', 'IA', 'IB', 'IC', 'TORQUE', 'NA', 'VELOCIDADE', 'TEMPO'];

def lerArquivo():
    df = pd.read_csv(caminhoArquivo, sep = ",", header = None);
    df.columns = colunas;
    return df;

def calcularMedia(dfColumn):
    return dfColumn.mean();

def calcularRms(dfColumn):
    aux = dfColumn**2;
    aux = aux.sum();
    aux = aux/dfColumn.count();
    return np.sqrt(abs(aux));
    
def calcularDht(dfColumn):
    aux = dfColumn;
    aux = aux.drop(aux.index[0]);
    aux = aux**2;
    aux = aux.sum();
    aux = aux/dfColumn[dfColumn.index[0]];
    return np.sqrt(abs(aux))/100;

df = lerArquivo();
df = df.iloc[-45000:];

print('id:%s' % id);

print('motor:%s' % motor)

print('torque:%.2f' % round(calcularMedia(df['TORQUE']), 2));
print('velocidade:%.2f' % round(calcularMedia(df['VELOCIDADE']), 2));

print('rms*', end = '')
print('vab;%.2f' % round(calcularRms(df['VAB']), 2), end = '\t');
print('vbc;%.2f' % round(calcularRms(df['VBC']), 2), end = '\t');
print('vca;%.2f' % round(calcularRms(df['VCA']), 2), end = '\t');
print('ia;%.2f' % round(calcularRms(df['IA']), 2), end = '\t');
print('ib;%.2f' % round(calcularRms(df['IB']), 2), end = '\t');
print('ic;%.2f' % round(calcularRms(df['IC']), 2));

print('dht*', end = '')
print('vab;%.2f' % round(calcularDht(df['VAB']), 2), end = '\t');
print('vbc;%.2f' % round(calcularDht(df['VBC']), 2), end = '\t');
print('vca;%.2f' % round(calcularDht(df['VCA']), 2), end = '\t');
print('ia;%.2f' % round(calcularDht(df['IA']), 2), end = '\t');
print('ib;%.2f' % round(calcularDht(df['IB']), 2), end = '\t');
print('ic;%.2f' % round(calcularDht(df['IC']), 2), end = '');