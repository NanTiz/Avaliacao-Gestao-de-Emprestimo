import java.util.List;

interface Notificacao {
    void enviarMensagem(String mensagem);
}

public abstract class MembroBiblioteca {
    protected String identificacao;
    protected int atrasoEmDias;

    public MembroBiblioteca(String identificacao, int atrasoEmDias) {
        this.identificacao = identificacao;
        this.atrasoEmDias = atrasoEmDias;
    }

    public abstract double calcularTaxaDeAtraso();

    public String obterResumo() {
        return String.format("Identificação: %s, Dias de Atraso: %d, Taxa de Atraso: R$ %.2f", identificacao, atrasoEmDias, calcularTaxaDeAtraso());
    }
}

class Estudante extends MembroBiblioteca {
    public Estudante(String identificacao, int atrasoEmDias) {
        super(identificacao, atrasoEmDias);
    }

    @Override
    public double calcularTaxaDeAtraso() {
        return atrasoEmDias * 0.50;
    }
}

class Docente extends MembroBiblioteca {
    public Docente(String identificacao, int atrasoEmDias) {
        super(identificacao, atrasoEmDias);
    }

    @Override
    public double calcularTaxaDeAtraso() {
        return atrasoEmDias * 0.25;
    }
}

class VisitanteTemporario extends MembroBiblioteca {
    public VisitanteTemporario(String identificacao, int atrasoEmDias) {
        super(identificacao, atrasoEmDias);
    }

    @Override
    public double calcularTaxaDeAtraso() {
        return atrasoEmDias * 1.00;
    }
}

class NotificacaoTerminal implements Notificacao {
    @Override
    public void enviarMensagem(String mensagem) {
        System.out.println("Mensagem (Terminal): " + mensagem);
    }
}

class NotificacaoWhatsAppSimulada implements Notificacao {
    @Override
    public void enviarMensagem(String mensagem) {
        System.out.println("Mensagem (WhatsApp Simulado): " + mensagem);
    }
}

interface CalculadoraDeTaxa {
    double calcularTaxa(MembroBiblioteca membro);
}

class TaxaFixaPorCategoria implements CalculadoraDeTaxa {
    @Override
    public double calcularTaxa(MembroBiblioteca membro) {
        if (membro instanceof Estudante) {
            return membro.atrasoEmDias * 0.50;
        } else if (membro instanceof Docente) {
            return membro.atrasoEmDias * 0.25;
        } else if (membro instanceof VisitanteTemporario) {
            return membro.atrasoEmDias * 1.00;
        }
        return 0.0;
    }
}

class TaxaProgressiva implements CalculadoraDeTaxa {
    @Override
    public double calcularTaxa(MembroBiblioteca membro) {
        double taxaBase = 0.50;
        int diasDeAtrasoComMultiplicador = membro.atrasoEmDias / 3;
        return taxaBase * Math.pow(1.1, diasDeAtrasoComMultiplicador) * membro.atrasoEmDias;
    }
}

class BibliotecaSistema {
    private Notificacao metodoNotificacao;
    private CalculadoraDeTaxa metodoDeTaxa;

    public BibliotecaSistema(Notificacao metodoNotificacao, CalculadoraDeTaxa metodoDeTaxa) {
        this.metodoNotificacao = metodoNotificacao;
        this.metodoDeTaxa = metodoDeTaxa;
    }

    public void processarTaxas(List<MembroBiblioteca> membros) {
        for (MembroBiblioteca membro : membros) {
            double taxa = metodoDeTaxa.calcularTaxa(membro);

            System.out.println(membro.obterResumo());

            metodoNotificacao.enviarMensagem("Notificação de taxa para " + membro.identificacao + ". Taxa de Atraso: R$ " + taxa);
        }
    }
}

class Principal {
    public static void main(String[] args) {
        List<MembroBiblioteca> membros = List.of(
                new Estudante("Lucas", 5),
                new Docente("Ana", 3),
                new VisitanteTemporario("Rafael", 7)
        );

        Notificacao metodoDeNotificacao = new NotificacaoWhatsAppSimulada();

        CalculadoraDeTaxa metodoDeTaxa = new TaxaProgressiva();

        BibliotecaSistema sistemaBiblioteca = new BibliotecaSistema(metodoDeNotificacao, metodoDeTaxa);

        sistemaBiblioteca.processarTaxas(membros);
    }
}