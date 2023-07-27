package com.example.lucrarelicenta2.BazaDeDate;

import com.example.lucrarelicenta2.componente.continut.profesor.ElementInformatiiGeneraleProfesor;
import com.example.lucrarelicenta2.componente.continut.ElementTema;
import com.example.lucrarelicenta2.componente.continut.containere.ElementInformatieGenerala;
import com.example.lucrarelicenta2.componente.continut.containere.ElementMaterialeSuport;
import com.example.lucrarelicenta2.componente.continut.containere.ElementProiect;
import com.example.lucrarelicenta2.componente.continut.containere.ElementTestGrila;
import com.example.lucrarelicenta2.componente.continut.profesor.ElementMaterialeSuportProfesor;
import com.example.lucrarelicenta2.componente.continut.profesor.ElementProiectProfesor;
import com.example.lucrarelicenta2.componente.continut.profesor.ElementTemeProfesor;
import com.example.lucrarelicenta2.componente.continut.profesor.ElementTestProfesor;
import com.example.lucrarelicenta2.componente.cursuri.Curs;
import com.example.lucrarelicenta2.componente.cursuri.CursProfesor;
import com.example.lucrarelicenta2.elemente.FisierMaterialeSuport;
import com.example.lucrarelicenta2.elemente.profesor.FisierMaterialeSuportProfesor;
import com.example.lucrarelicenta2.elemente.profesor.FisiereTemeProfesor;
import com.example.lucrarelicenta2.elemente.profesor.TemaTrimisaProfesor;
import com.example.lucrarelicenta2.modele.Intrebare;
import com.example.lucrarelicenta2.modele.Profesor;
import com.example.lucrarelicenta2.modele.Utilizator;

import java.io.InputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

import javafx.scene.control.Label;
import javafx.stage.Stage;

public class BazaDeDate {
    String urlBazaDeDate = "jdbc:postgresql://localhost:5432/LucrareLicenta";
    String numeUtilizatorBazaDeDate = "postgres";
    String parolaUtilizatorBazaDeDate = "postgres";
    private Connection conexiuneBazaDeDate;

    public BazaDeDate() {
        try {
            conexiuneBazaDeDate = DriverManager.getConnection(urlBazaDeDate, numeUtilizatorBazaDeDate, parolaUtilizatorBazaDeDate);
            TimeZone.setDefault(TimeZone.getTimeZone("Europe/Bucharest"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //conectare la baza de date pentru inregistare
    public boolean creareUtilizator(String numeUtilizator, String nume, String prenume, String parola, String email) {
        String inserareUtilizator = "insert into \"Utilizatori\" (\"numeUtilizator\", nume, prenume, parola, \"e-mail\") values (?, ?, ?, ?, ?);";
        try {
            PreparedStatement SQLStatement = this.conexiuneBazaDeDate.prepareStatement(inserareUtilizator);
            SQLStatement.setString(1, numeUtilizator);
            SQLStatement.setString(2, nume);
            SQLStatement.setString(3, prenume);
            SQLStatement.setString(4, parola);
            SQLStatement.setString(5, email);
            int liniiCreate = SQLStatement.executeUpdate();
            if (!(liniiCreate > 0)) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Utilizator autenficareUtilizator(String numeUtilizator, String parola) {
        Utilizator utilizatorGasit = null;
        String selectareInformatie = "select * from \"Utilizatori\" where \"numeUtilizator\" = ? and parola = ?";
        try {
            PreparedStatement SQLStatement = this.conexiuneBazaDeDate.prepareStatement(selectareInformatie);
            SQLStatement.setString(1, numeUtilizator);
            SQLStatement.setString(2, parola);
            ResultSet resultSet = SQLStatement.executeQuery();
            if (resultSet.next()) {
                long idUtilizator = resultSet.getLong("idUtilizator");
                String nume = resultSet.getString("nume");
                String prenume = resultSet.getString("prenume");
                String email = resultSet.getString("e-mail");
                utilizatorGasit = new Utilizator(idUtilizator, numeUtilizator, nume, prenume, parola, email);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return utilizatorGasit;
    }

    public List<Curs> preluareListaCursuri(Utilizator utilizatorAutentificat) {
        List<Curs> listaCursuri = new ArrayList<>();
        String selectareInformatie = "select T2.\"idCurs\", T2.\"idProfesor\", T3.\"numeProfesor\", T3.\"prenumeProfesor\", " +
                "T2.\"titluCurs\", T2.\"descriereaCursului\" from \"Inscrieri\" T1 join \"Cursuri\" T2 on T2.\"idCurs\" = T1.\"idCurs\" " +
                "join \"Profesori\" T3 on T2.\"idProfesor\" = T3.\"idProfesor\" join \"Utilizatori\" U on U.\"idUtilizator\" = " +
                "T1.\"idUtilizator\" where U.\"idUtilizator\" = ?;";
        try {
            PreparedStatement SQLStatementCursuri = this.conexiuneBazaDeDate.prepareStatement(selectareInformatie);
            long idUtilizator = utilizatorAutentificat.getIdUtilizator();
            SQLStatementCursuri.setLong(1, idUtilizator);
            ResultSet resultSetCursuri = SQLStatementCursuri.executeQuery();
            while (resultSetCursuri.next()) {
                long idCurs = resultSetCursuri.getLong("idCurs");
//                PreparedStatement SQLStatementTeme = this.conexiuneBazaDeDate.prepareStatement("SELECT T2.\"titluElement\" FROM \"Capitole\" T1 JOIN " +
//                        "\"Teme\" T2 ON T1.\"idCapitol\" = T2.\"idCapitol\" WHERE \"idCurs\" = ? AND \"titluCapitol\" = 'Teme' and \"idUtilizator\" = ?;");
                PreparedStatement SQLStatementTeme = this.conexiuneBazaDeDate.prepareStatement("select * from \"Capitole\" T1 " +
                        "join \"Teme\" T2 on T1.\"idCapitol\" = T2.\"idCapitol\" join \"StatusTeme\" T3 on T2.\"idElement\" = T3.\"idTema\" " +
                        "where \"idUtilizator\" = ? and \"idCurs\" = ? and \"statusTema\" != 'Trimisa';");
                SQLStatementTeme.setLong(1, utilizatorAutentificat.getIdUtilizator());
                SQLStatementTeme.setLong(2, idCurs);
                ResultSet resultSetTeme = SQLStatementTeme.executeQuery();
                List<String> listaTitluriTeme = new ArrayList<>();
                while (resultSetTeme.next()) {
                    listaTitluriTeme.add(resultSetTeme.getString("titluElement"));
                }
                String titluCurs = resultSetCursuri.getString("titluCurs");
                String subtitluCurs = resultSetCursuri.getString("descriereaCursului");
                String numeProfesor = resultSetCursuri.getString("numeProfesor");
                String prenumeProfesor = resultSetCursuri.getString("prenumeProfesor");

                PreparedStatement SQLStatementLectii = conexiuneBazaDeDate.prepareStatement("select * from \"Lectii\" where \"idCurs\" = ?;");
                SQLStatementLectii.setInt(1, (int) idCurs);
                ResultSet resultSetLectii = SQLStatementLectii.executeQuery();
                List<String> listaLectii = new ArrayList<>();
                while (resultSetLectii.next()) {
                    listaLectii.add(resultSetLectii.getString("titluLectie"));
                }
                listaCursuri.add(new Curs(idCurs, titluCurs, subtitluCurs, numeProfesor + " " + prenumeProfesor,
                        listaLectii, listaTitluriTeme.toArray(new String[0])));
                listaTitluriTeme.clear();
//                listaLectii.clear();
                resultSetTeme.close();
                SQLStatementTeme.close();
            }
            resultSetCursuri.close();
            SQLStatementCursuri.close();
            this.conexiuneBazaDeDate.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return listaCursuri;
    }

    public List<ElementTema> preluareListaTeme(long idCapitol, Utilizator utilizatorAutentificat, Stage radacinaAplicatiei) throws SQLException {
        List<ElementTema> listaTeme = new ArrayList<>();
        PreparedStatement preparedStatementTeme = this.conexiuneBazaDeDate.prepareStatement("SELECT * FROM \"InscrieriTeme\" T1 " +
                "JOIN \"Teme\" T2 ON T2.\"idElement\" = T1.\"idTema\" JOIN \"StatusTeme\" T3 " +
                "ON T2.\"idElement\" = T3.\"idTema\" WHERE T1.\"idUtilizator\" = ? AND T2.\"idCapitol\" = ?;");
        preparedStatementTeme.setLong(1, utilizatorAutentificat.getIdUtilizator());
        preparedStatementTeme.setLong(2, idCapitol);
        ResultSet resultSetTeme = preparedStatementTeme.executeQuery();
        while (resultSetTeme.next()) {
            long idElement = resultSetTeme.getLong("idElement");
            String titluElement = resultSetTeme.getString("titluElement");
            Timestamp termenPostare = resultSetTeme.getTimestamp("termenPostare");
            System.out.println("termenPostare = " + termenPostare);
            Timestamp termenLimita = resultSetTeme.getTimestamp("termenLimita");
            System.out.println("termenLimita = " + termenLimita);
            String taskElement = resultSetTeme.getString("taskElement");
            listaTeme.add(new ElementTema(idElement, titluElement, idCapitol, termenPostare,
                    termenLimita, taskElement, utilizatorAutentificat, radacinaAplicatiei));
        }
        resultSetTeme.close();
        preparedStatementTeme.close();
        return listaTeme;
    }

    public List<ElementProiect> preluareProiecteUtilizator(long idCapitol, Stage radacinaAplicatiei, Utilizator utilizatorAutentificat) throws SQLException {
        List<ElementProiect> listaElementeProiecte = new ArrayList<>();
        PreparedStatement preparedStatementProiecte = conexiuneBazaDeDate.prepareStatement("select * from \"Proiecte\" where \"idCapitol\" = ?;");
        preparedStatementProiecte.setLong(1, idCapitol);
        ResultSet resultSetProiecte = preparedStatementProiecte.executeQuery();
        while (resultSetProiecte.next()) {
            long idProiect = resultSetProiecte.getLong("idProiect");
            String titluProiect = resultSetProiecte.getString("titluProiect");
            Timestamp termenPredare = resultSetProiecte.getTimestamp("termenPredare");
            System.out.println("termenPredare = " + termenPredare);
            String obiectiveProiect = "";
            PreparedStatement preparedStatementObiective = conexiuneBazaDeDate.prepareStatement("select * from \"ObiectiveProiect\" where \"idProiect\" = ?;");
            preparedStatementObiective.setLong(1, idProiect);
            ResultSet resultSetObiective = preparedStatementObiective.executeQuery();
            if (resultSetObiective.next()) {
                obiectiveProiect = resultSetObiective.getString("obiectivProiect");
            }
            PreparedStatement preparedStatementStatusProiect = conexiuneBazaDeDate.prepareStatement("select \"statusProiect\" " +
                    "from \"StatusProiect\" where \"idProiect\" = ? and \"idUtilizator\" = ?;");
            preparedStatementStatusProiect.setLong(1, idProiect);
            preparedStatementStatusProiect.setLong(2, utilizatorAutentificat.getIdUtilizator());
            ResultSet resultSetStatusProiect = preparedStatementStatusProiect.executeQuery();
            String statusProiect = resultSetStatusProiect.next() ? resultSetStatusProiect.getString("statusProiect") : "Necunoscut";
            List<String> listaObiective = Arrays.stream(obiectiveProiect.split("\\\\n")).toList();
            listaElementeProiecte.add(new ElementProiect(radacinaAplicatiei, idProiect, idCapitol, titluProiect,
                    termenPredare, listaObiective, statusProiect, utilizatorAutentificat));
        }
        return listaElementeProiecte;
    }

    public List<ElementInformatieGenerala> preluareElementeInformatiiGenerale(long idCapitol, Utilizator utilizatorAutentificat) throws SQLException {
        List<ElementInformatieGenerala> listaInformatiiGenerale = new ArrayList<>();
        PreparedStatement preparedStatementInformatiiGenerale = conexiuneBazaDeDate.prepareStatement("select * from \"InformatiiGenerale\" where \"idCapitol\" = ?;");
        preparedStatementInformatiiGenerale.setLong(1, idCapitol);
        ResultSet resultSetInformatiiGenerale = preparedStatementInformatiiGenerale.executeQuery();
        while (resultSetInformatiiGenerale.next()) {
            long idElement = resultSetInformatiiGenerale.getLong("idElement");
            String titluElement = resultSetInformatiiGenerale.getString("titluElement");
            String mesajElement = resultSetInformatiiGenerale.getString("mesajElement");
            listaInformatiiGenerale.add(new ElementInformatieGenerala(idElement, idCapitol, titluElement, mesajElement, utilizatorAutentificat));
        }
        return listaInformatiiGenerale;
    }

    public List<ElementMaterialeSuport> preluareElementeMaterialeSuport(long idCapitol, Stage radacinaAplicatiei, Utilizator utilizatorAutentificat) throws SQLException {
        List<ElementMaterialeSuport> listaElementeMaterialeSuport = new ArrayList<>();
        PreparedStatement preparedStatementMaterialeSuport = conexiuneBazaDeDate.prepareStatement("select * from \"MaterialeSuport\" where \"idCapitol\" = ?;");
        preparedStatementMaterialeSuport.setLong(1, idCapitol);
        ResultSet resultSetMaterialeSuport = preparedStatementMaterialeSuport.executeQuery();
        while (resultSetMaterialeSuport.next()) {
            long idElement = resultSetMaterialeSuport.getLong("idElement");
            String titluElement = resultSetMaterialeSuport.getString("titluElement");
            String mesajElement = resultSetMaterialeSuport.getString("mesajElement");
            List<FisierMaterialeSuport> listaFisiereMaterialeSuport = new ArrayList<>();
            PreparedStatement preparedStatementFisiereMaterialeSuport = conexiuneBazaDeDate.prepareStatement("select * from \"FisiereMaterialeSuport\" where \"idElement\" = ?;");
            preparedStatementFisiereMaterialeSuport.setLong(1, idElement);
            ResultSet resultSetFisiereMaterialeSuport = preparedStatementFisiereMaterialeSuport.executeQuery();
            while (resultSetFisiereMaterialeSuport.next()) {
                long idFisier = resultSetFisiereMaterialeSuport.getLong("idFisier");
                String numeFisier = resultSetFisiereMaterialeSuport.getString("numeFisier");
                InputStream continutFisier = resultSetFisiereMaterialeSuport.getBinaryStream("continutFisier");
                listaFisiereMaterialeSuport.add(new FisierMaterialeSuport(idFisier, idCapitol, numeFisier, continutFisier));
            }
            listaElementeMaterialeSuport.add(new ElementMaterialeSuport(idElement, idCapitol, titluElement, mesajElement, listaFisiereMaterialeSuport, radacinaAplicatiei, utilizatorAutentificat));
        }
        return listaElementeMaterialeSuport;
    }

    public List<ElementTestGrila> getTesteUtilizator(long idCapitol, Utilizator utilizatorAutentificat) throws SQLException {
        List<ElementTestGrila> listaTesteGrila = new ArrayList<>();
        PreparedStatement preparedStatementTeste = this.conexiuneBazaDeDate.prepareStatement("select * from \"Teste\" where \"idCapitol\" = ?;");
        preparedStatementTeste.setLong(1, idCapitol);
        ResultSet resultSetTeste = preparedStatementTeste.executeQuery();
        while (resultSetTeste.next()) {
            long idElement = resultSetTeste.getLong("idElement");
            String titluElement = resultSetTeste.getString("titluElement");
            List<Intrebare> listaIntrebari = new ArrayList<>();
            PreparedStatement preparedStatementIntrebari = this.conexiuneBazaDeDate.prepareStatement("select * from \"IntrebariTeste\" where \"idTest\" = ?;");
            preparedStatementIntrebari.setLong(1, idElement);
            ResultSet resultSetIntrebari = preparedStatementIntrebari.executeQuery();
            while (resultSetIntrebari.next()) {
                List<String> listaRaspunsuriIntrebare = new ArrayList<>();
                long idIntrebare = resultSetIntrebari.getLong("idIntrebare");
                String textIntrebare = resultSetIntrebari.getString("textIntrebare");
                PreparedStatement preparedStatementRaspunsuri = this.conexiuneBazaDeDate.prepareStatement("select * from \"Raspunsuri\" where \"idIntrebare\" = ?;");
                preparedStatementRaspunsuri.setLong(1, idIntrebare);
                ResultSet resultSetRaspunsuri = preparedStatementRaspunsuri.executeQuery();
                while (resultSetRaspunsuri.next()) {
                    listaRaspunsuriIntrebare.add(resultSetRaspunsuri.getString("textRaspuns"));
                }
                PreparedStatement preparedStatementRaspunsCorect = this.conexiuneBazaDeDate.prepareStatement("select * from \"RaspunsuriCorecte\" where \"idIntrebare\" = ?;");
                preparedStatementRaspunsCorect.setLong(1, idIntrebare);
                ResultSet resultSetRaspunsCorect = preparedStatementRaspunsCorect.executeQuery();
                if (resultSetRaspunsCorect.next()) {
                    String raspunsCorect = resultSetRaspunsCorect.getString("raspunsCorect");
                    listaIntrebari.add(new Intrebare(idIntrebare, textIntrebare, listaRaspunsuriIntrebare, raspunsCorect));
                }
            }
            listaTesteGrila.add(new ElementTestGrila(idElement, idCapitol, titluElement, listaIntrebari, utilizatorAutentificat));
        }
        return listaTesteGrila;
    }

    public Connection getConexiuneBazaDeDate() {
        return conexiuneBazaDeDate;
    }

    public List<Label> preluareMesajeElement(long idCapitol, long idElement) throws SQLException {
        List<Label> mesajeUtilizatori = new ArrayList<>();
        PreparedStatement preparedStatementMesaje = conexiuneBazaDeDate.prepareStatement("select T1.*, T2.nume, T2.prenume " +
                "from \"MesajeUtilizatori\" T1 join \"Utilizatori\" T2 on T2.\"idUtilizator\" = T1.\"idUtilizator\" " +
                "where \"idCapitol\" = ? and \"idElement\" = ? order by \"timeStampMesaj\";");
        preparedStatementMesaje.setLong(1, idCapitol);
        preparedStatementMesaje.setLong(2, idElement);
        ResultSet resultSetMesaje = preparedStatementMesaje.executeQuery();
        while (resultSetMesaje.next()) {
            String timeStampMesajFormatat = new SimpleDateFormat("HH:mm").format(resultSetMesaje.getTimestamp("timeStampMesaj"));
            String mesajUtilizator = resultSetMesaje.getString("mesajUtilizator");
            String numeFamilieUtilizator = resultSetMesaje.getString("nume");
            String prenumeUtilizator = resultSetMesaje.getString("prenume");
            mesajeUtilizatori.add(new Label(timeStampMesajFormatat + " " + numeFamilieUtilizator + " "
                    + prenumeUtilizator + ": " + mesajUtilizator));
        }
        return mesajeUtilizatori;
    }

    public List<Label> preluareMesajeProfesor(long idCapitol, long idElement, long idProfesor) throws SQLException {
        List<Label> mesajeProfesor = new ArrayList<>();
        PreparedStatement preparedStatementMesaje = conexiuneBazaDeDate.prepareStatement("select * from \"MesajeProfesori\" T1 " +
                "join \"Profesori\" T2 on T1.\"idProfesor\" = T2.\"idProfesor\" " +
                "where \"idElement\" = ? and \"idCapitol\" = ? and T1.\"idProfesor\" = ?;");
        preparedStatementMesaje.setLong(1, idElement);
        preparedStatementMesaje.setLong(2, idCapitol);
        preparedStatementMesaje.setLong(3, idProfesor);
        ResultSet resultSetMesaje = preparedStatementMesaje.executeQuery();
        while (resultSetMesaje.next()) {
            String timeStampMesajFormatat = new SimpleDateFormat("HH:mm").format(resultSetMesaje.getTimestamp("timeStampMesaj"));
            String mesajProfesor = resultSetMesaje.getString("mesajProfesor");
            String numeFamilieUtilizator = resultSetMesaje.getString("numeProfesor");
            String prenumeUtilizator = resultSetMesaje.getString("prenumeProfesor");
            mesajeProfesor.add(new Label(timeStampMesajFormatat + " " + numeFamilieUtilizator + " "
                    + prenumeUtilizator + ": " + mesajProfesor));
        }
        return mesajeProfesor;
    }

    public List<CursProfesor> getCursuriProfesor(long idProfesor) {
        List<CursProfesor> titluriCursuriProfesor = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = conexiuneBazaDeDate.prepareStatement("select * from \"Cursuri\" where \"idProfesor\" = ?;");
            preparedStatement.setLong(1, idProfesor);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long idCurs = resultSet.getLong("idCurs");
                String titluCurs = resultSet.getString("titluCurs");
                String descriereaCursului = resultSet.getString("descriereaCursului");
                titluriCursuriProfesor.add(new CursProfesor(idCurs, titluCurs, descriereaCursului));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return titluriCursuriProfesor;
    }

    public List<Integer> getStatisticiCurs(long idCurs) {
        int statisticaInformatiiGenerale = 0, statisticaMaterialeSuport = 0, statisticaTeme = 0, statisticaProiecte = 0;
        Connection conexiuneBD = getConexiuneBazaDeDate();
        if (conexiuneBD != null) {
            try {
                PreparedStatement preparedStatementInformatiiGenerale = conexiuneBD.prepareStatement("select count(*) as \"TotalElementeInformatiiGenerale\" " +
                        "from \"Capitole\" T1 join \"InformatiiGenerale\" T2 on T1.\"idCapitol\" = T2.\"idCapitol\" " +
                        "where \"idCurs\" = ?;");
                preparedStatementInformatiiGenerale.setLong(1, idCurs);
                ResultSet resultSetInformatiiGenerale = preparedStatementInformatiiGenerale.executeQuery();
                if (resultSetInformatiiGenerale.next()) {
                    statisticaInformatiiGenerale = resultSetInformatiiGenerale.getInt("TotalElementeInformatiiGenerale");
                }
                PreparedStatement preparedStatementMaterialeSuport = conexiuneBD.prepareStatement("select count(*) as \"TotalElementeMaterialeSuport\" " +
                        "from \"Capitole\" T1 join \"MaterialeSuport\" T2 on T1.\"idCapitol\" = T2.\"idCapitol\" " +
                        "where \"idCurs\" = ?;");
                preparedStatementMaterialeSuport.setLong(1, idCurs);
                ResultSet resultSetMaterialeSuport = preparedStatementMaterialeSuport.executeQuery();
                if (resultSetMaterialeSuport.next()) {
                    statisticaMaterialeSuport = resultSetMaterialeSuport.getInt("TotalElementeMaterialeSuport");
                }
                PreparedStatement preparedStatementTeme = conexiuneBD.prepareStatement("select count(*) as \"TotalElementeTeme\" " +
                        "from \"Capitole\" T1 join \"Teme\" T2 on T1.\"idCapitol\" = T2.\"idCapitol\" where \"idCurs\" = ?;");
                preparedStatementTeme.setLong(1, idCurs);
                ResultSet resultSetTeme = preparedStatementTeme.executeQuery();
                if (resultSetTeme.next()) {
                    statisticaTeme = resultSetTeme.getInt("TotalElementeTeme");
                }
                PreparedStatement preparedStatementProiecte = conexiuneBD.prepareStatement("select count(*) as \"TotalElementeProiecte\" " +
                        "from \"Capitole\" T1 join \"Proiecte\" T2 on T1.\"idCapitol\" = T2.\"idCapitol\" where T1.\"idCurs\" = ?;");
                preparedStatementProiecte.setLong(1, idCurs);
                ResultSet resultSetProiecte = preparedStatementProiecte.executeQuery();
                if (resultSetProiecte.next()) {
                    statisticaProiecte = resultSetProiecte.getInt("TotalElementeProiecte");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<>(List.of(statisticaInformatiiGenerale, statisticaMaterialeSuport, statisticaTeme, statisticaProiecte));
    }

    public List<ElementInformatiiGeneraleProfesor> getInformatiiGeneraleProfesor(long idCurs, Profesor profesorConectat) {
        List<ElementInformatiiGeneraleProfesor> informatiiGenerale = new ArrayList<>();
        if (conexiuneBazaDeDate != null) {
            try {
                PreparedStatement preparedStatement = conexiuneBazaDeDate.prepareStatement("select T2.* from \"Capitole\" T1 " +
                        "join \"InformatiiGenerale\" T2 on T1.\"idCapitol\" = T2.\"idCapitol\" where T1.\"idCurs\" = ?;");
                preparedStatement.setLong(1, idCurs);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    long idElement = resultSet.getLong("idElement");
                    long idCapitol = resultSet.getLong("idCapitol");
                    String titluElement = resultSet.getString("titluElement");
                    String mesajElement = resultSet.getString("mesajElement");
                    informatiiGenerale.add(new ElementInformatiiGeneraleProfesor(idElement, idCapitol, titluElement, mesajElement, profesorConectat));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return informatiiGenerale;
    }

    public List<ElementMaterialeSuportProfesor> getMaterialeSuportProfesor(long idCurs, Profesor profesorConectat, Stage fereastraRadacina) {
        List<ElementMaterialeSuportProfesor> informatiiGenerale = new ArrayList<>();
        if (conexiuneBazaDeDate != null) {
            try {
                PreparedStatement preparedStatement = conexiuneBazaDeDate.prepareStatement("select T2.* from \"Capitole\" T1 " +
                        "join \"MaterialeSuport\" T2 on T1.\"idCapitol\" = T2.\"idCapitol\" where T1.\"idCurs\" = ?;");
                preparedStatement.setLong(1, idCurs);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    long idElement = resultSet.getLong("idElement");
                    long idCapitol = resultSet.getLong("idCapitol");
                    String titluElement = resultSet.getString("titluElement");
                    String mesajElement = resultSet.getString("mesajElement");
                    informatiiGenerale.add(new ElementMaterialeSuportProfesor(idElement, idCapitol, titluElement,
                            mesajElement, profesorConectat, fereastraRadacina));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return informatiiGenerale;
    }

    public List<FisierMaterialeSuportProfesor> getFisiereMaterialeSuportProfesor(long idElement) {
        List<FisierMaterialeSuportProfesor> listaFisiere = new ArrayList<>();
        if (conexiuneBazaDeDate != null) {
            try {
                PreparedStatement preparedStatement = conexiuneBazaDeDate.prepareStatement("select * " +
                        "from \"FisiereMaterialeSuport\" where \"idElement\" = ?;");
                preparedStatement.setLong(1, idElement);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    long idFisier = resultSet.getLong("idFisier");
                    String numeFisier = resultSet.getString("numeFisier");
                    InputStream continutFisier = resultSet.getBinaryStream("continutFisier");
                    listaFisiere.add(new FisierMaterialeSuportProfesor(idFisier, idElement, numeFisier, continutFisier));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return listaFisiere;
    }

    public List<ElementTemeProfesor> getElementeTemeProfesor(long idCurs, Profesor profesorConectat, Stage fereastraRadacina) {
        List<ElementTemeProfesor> listaTeme = new ArrayList<>();
        if (conexiuneBazaDeDate != null) {
            try {
                PreparedStatement preparedStatement = conexiuneBazaDeDate.prepareStatement("select \"idElement\", " +
                        "T1.\"idCapitol\", \"titluElement\", \"taskElement\", \"termenPostare\", coalesce(\"termenLimita\", " +
                        "\"termenPostare\" + interval '1 day')::DATE as \"termenLimita\" from \"Capitole\" T1 join \"Teme\" T2 " +
                        "on T1.\"idCapitol\" = T2.\"idCapitol\" where T1.\"idCurs\" = ?;");
                preparedStatement.setLong(1, idCurs);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    long idTema = resultSet.getLong("idElement");
                    long idCapitol = resultSet.getLong("idCapitol");
                    String titluTema = resultSet.getString("titluElement");
                    String cerintaTema = resultSet.getString("taskElement");
                    Date termenCreare = resultSet.getDate("termenPostare");
                    Date termenLimita = resultSet.getDate("termenLimita");
                    listaTeme.add(new ElementTemeProfesor(idTema, idCapitol, titluTema, cerintaTema,
                            termenCreare, termenLimita, profesorConectat, fereastraRadacina));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return listaTeme;
    }

    public List<TemaTrimisaProfesor> getTemeTrimise(long idTema, long idCapitol) {
        List<TemaTrimisaProfesor> listaTemeTrimise = new ArrayList<>();
        if (conexiuneBazaDeDate != null) {
            try {
                PreparedStatement preparedStatement = conexiuneBazaDeDate.prepareStatement("select \"idElement\", \"statusTema\", \"notaTema\", " +
                        "T2.\"idUtilizator\", \"numeUtilizator\", nume, prenume, \"idCapitol\" " +
                        "from \"Teme\" T1 join \"StatusTeme\" T2 on T1.\"idElement\" = T2.\"idTema\" " +
                        "join \"Utilizatori\" T3 on T3.\"idUtilizator\" = T2.\"idUtilizator\" where \"idElement\" = ? and \"idCapitol\" = ?;");
                preparedStatement.setLong(1, idTema);
                preparedStatement.setLong(2, idCapitol);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    long idElement = resultSet.getLong("idElement");
                    long idUtilizator = resultSet.getLong("idUtilizator");
                    String statusTema = resultSet.getString("statusTema");
                    int notaTema = resultSet.getInt("notaTema");
                    String numeUtilizator = resultSet.getString("numeUtilizator");
                    String numeFamilie = resultSet.getString("nume");
                    String prenumeUtilizator = resultSet.getString("prenume");
                    List<FisiereTemeProfesor> listaFisiere = new ArrayList<>();
                    PreparedStatement preparedStatementFisiere = conexiuneBazaDeDate.prepareStatement("select * from \"FisiereTeme\" " +
                            "where \"idTema\" = ? and \"idUtilizator\" = ?;");
                    preparedStatementFisiere.setLong(1, idElement);
                    preparedStatementFisiere.setLong(2, idUtilizator);
                    ResultSet resultSetTeme = preparedStatementFisiere.executeQuery();
                    while (resultSetTeme.next()) {
                        long idFisier = resultSetTeme.getLong("idFisier");
                        String numeFisier = resultSetTeme.getString("numeFisier");
                        InputStream continutFisier = resultSetTeme.getBinaryStream("continutFisier");
                        listaFisiere.add(new FisiereTemeProfesor(idFisier, idTema, idCapitol, idUtilizator, numeFisier, continutFisier));
                    }
                    listaTemeTrimise.add(new TemaTrimisaProfesor(idElement, idCapitol, idUtilizator, statusTema, notaTema,
                            numeUtilizator, numeFamilie, prenumeUtilizator, listaFisiere));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return listaTemeTrimise;
    }

    public List<ElementProiectProfesor> getProiecteProfesor(long idCurs, Stage fereastraRadacina) {
        List<ElementProiectProfesor> listaProiecte = new ArrayList<>();
        if (conexiuneBazaDeDate != null) {
            try {
                PreparedStatement preparedStatement = conexiuneBazaDeDate.prepareStatement("select * from \"Proiecte\" where \"idCurs\" = ?;");
                preparedStatement.setLong(1, idCurs);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    long idProiect = resultSet.getLong("idProiect");
                    long idCapitol = resultSet.getLong("idCapitol");
                    String titluProiect = resultSet.getString("titluProiect");
                    Date termenPredare = resultSet.getDate("termenPredare");
                    listaProiecte.add(new ElementProiectProfesor(idProiect, idCurs, idCapitol, titluProiect, termenPredare, fereastraRadacina));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return listaProiecte;
    }

    public List<ElementTestProfesor> getTesteProfesor(long idCurs) {
        List<ElementTestProfesor> testeProfesor = new ArrayList<>();
        try {
            PreparedStatement preparedStatementTeste = this.conexiuneBazaDeDate.prepareStatement("select * " +
                    "from \"Teste\" where \"idCurs\" = ?;");
            preparedStatementTeste.setLong(1, idCurs);
            ResultSet resultSetTeste = preparedStatementTeste.executeQuery();
            while (resultSetTeste.next()) {
                long idElement = resultSetTeste.getLong("idElement");
                String titluElement = resultSetTeste.getString("titluElement");
                long idCapitol = resultSetTeste.getLong("idCapitol");
                List<Intrebare> listaIntrebari = new ArrayList<>();
                PreparedStatement preparedStatementIntrebari = this.conexiuneBazaDeDate.prepareStatement("select * " +
                        "from \"IntrebariTeste\" where \"idTest\" = ?;");
                preparedStatementIntrebari.setLong(1, idElement);
                ResultSet resultSetIntrebari = preparedStatementIntrebari.executeQuery();
                while (resultSetIntrebari.next()) {
                    List<String> listaRaspunsuriIntrebare = new ArrayList<>();
                    long idIntrebare = resultSetIntrebari.getLong("idIntrebare");
                    String textIntrebare = resultSetIntrebari.getString("textIntrebare");
                    PreparedStatement preparedStatementRaspunsuri = this.conexiuneBazaDeDate.prepareStatement("select * " +
                            "from \"Raspunsuri\" where \"idIntrebare\" = ?;");
                    preparedStatementRaspunsuri.setLong(1, idIntrebare);
                    ResultSet resultSetRaspunsuri = preparedStatementRaspunsuri.executeQuery();
                    while (resultSetRaspunsuri.next()) {
                        listaRaspunsuriIntrebare.add(resultSetRaspunsuri.getString("textRaspuns"));
                    }
                    PreparedStatement preparedStatementRaspunsCorect = this.conexiuneBazaDeDate.prepareStatement("select * " +
                            "from \"RaspunsuriCorecte\" where \"idIntrebare\" = ?;");
                    preparedStatementRaspunsCorect.setLong(1, idIntrebare);
                    ResultSet resultSetRaspunsCorect = preparedStatementRaspunsCorect.executeQuery();
                    if (resultSetRaspunsCorect.next()) {
                        String raspunsCorect = resultSetRaspunsCorect.getString("raspunsCorect");
                        listaIntrebari.add(new Intrebare(idIntrebare, textIntrebare, listaRaspunsuriIntrebare, raspunsCorect));
                    }
                }
                testeProfesor.add(new ElementTestProfesor(idElement, idCapitol, titluElement, listaIntrebari));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return testeProfesor;
    }
}
