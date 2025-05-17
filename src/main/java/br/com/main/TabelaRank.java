package br.com.main;

public class TabelaRank {
    private int posicao;
    private String cidade;
    private int casosConfirmados;

    public TabelaRank(int posicao, String cidade, int casosConfirmados) {
        this.posicao = posicao;
        this.cidade = cidade;
        this.casosConfirmados = casosConfirmados;
    }

    // POSICAO
        public int getPosicao() {
            return posicao;
        }
        public void setPosicao(int posicao) {
            this.posicao = posicao;
        }

    // CIDADE
        public String getCidade() {
            return cidade;
        }
        public void setCidade(String cidade) {
            this.cidade = cidade;
        }

    // CASOS CONFIRMADOS
        public int getCasosConfirmados() {
            return casosConfirmados;
        }
        public void setCasosConfirmados(int casosConfirmados) {
            this.casosConfirmados = casosConfirmados;
        }
}
