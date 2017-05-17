package app.bluetappresumido.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class BancoDeDados extends SQLiteOpenHelper {

    // Declaracao das variaveis
    private static final String DATABASE = "BlueAppDatabase";
    private static final int VERSAO = 1;
    private static String TABELA = "";
    private final String TABELATURMAS = "Turmas";
    static SQLiteDatabase bancoDeDados;

    // Construtor
    public BancoDeDados(Context context) {
        super(context, DATABASE, null, VERSAO);
        bancoDeDados = this.getWritableDatabase();
    }

    // Metodo responsavel pela criacao da estrutura do BD
    @Override
    public void onCreate(SQLiteDatabase database) {

    }

    // Metodo responsavel pela atualizacao do BD
    public void onUpgrade(SQLiteDatabase database, int versaoAntiga, int versaoNova) {
        //String query = "DROP TABLE IF EXISTS " + TABELA;
        //bancoDeDados.execSQL(query);
        //criarTabelaAluno(TABELA);
    }

    //##################################### METODOS RESPONSÁVEIS PELA CLASSE TURMA ################################################################
    //#############################################################################################################################################

    //Método responsável pela criação da tabela que armazena o nome das turmas criadas e a carga horária de cada uma.
    public void criarTabelaTurmas(){
        String query = "CREATE TABLE IF NOT EXISTS " + TABELATURMAS + " (id INTEGER PRIMARY KEY AUTOINCREMENT, nomeTurma TEXT, cargaHoraria INTEGER)";
        bancoDeDados.execSQL(query);
    }

    // Metodo responsavel pela insercao de uma turma no BD.
    public void inserirTurmaBD(Turma turma, String nomeTabela) {
        TABELA = nomeTabela;
        ContentValues values = new ContentValues();
        values.put("nomeTurma", turma.getNome());
        values.put("cargaHoraria", turma.getCargaHoraria());
        bancoDeDados.insert(TABELA, null, values);
    }

    // Metodo responsavel pela remocao da turma no BD
    public void removerTurmaBD(String nomeTurma) {
        bancoDeDados.delete(TABELATURMAS, "nomeTurma = '" + nomeTurma + "'", null);
        String query = "DROP TABLE IF EXISTS " + nomeTurma;
        bancoDeDados.execSQL(query);
        query = "DROP TABLE IF EXISTS " + nomeTurma + "Registro";
        bancoDeDados.execSQL(query);
    }

    //Método responsável por capturar o nome de todas as turmas existentes no BD.
    public ArrayList<String> getNomesTurmas(){
        ArrayList<String> tableNames = new ArrayList<String>();
        String query = "SELECT * FROM " + TABELATURMAS;
        Cursor cursor = bancoDeDados.rawQuery(query, null);

        try{
            while(cursor.moveToNext()) {
                String nome = cursor.getString(1);
                tableNames.add(nome);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }

        return tableNames;
    }

    //Método responsável por capturar a carga horária de uma determinada turma existente no BD.
    public int getCargaHoraria(String nomeTurma){
        String query = "SELECT * FROM " + TABELATURMAS;
        Cursor cursor = bancoDeDados.rawQuery(query, null);
        Turma turma = new Turma();

        try{
            while(cursor.moveToNext()){
                turma.setNome(cursor.getString(1));
                turma.setCargaHoraria(cursor.getInt(2));
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return turma.getCargaHoraria();
    }
    //###################################### FIM DOS METODOS RESPONSÁVEIS PELA CLASSE TURMA ###########################################################
    //#################################################################################################################################################




    //##################################### METODOS RESPONSÁVEIS PELA CLASSE ALUNO ################################################################
    //#############################################################################################################################################

    // Metodo responsavel pela criacao da tabela contendo os dados dos alunos
    public void criarTabelaAluno(String nomeTabela) {
        TABELA = nomeTabela;
        String query = "CREATE TABLE IF NOT EXISTS " + TABELA + " (id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT, matricula INTEGER, bluetoothAddress TEXT, frequencia TEXT, curso TEXT, email TEXT)";
        bancoDeDados.execSQL(query);
    }

    // Metodo responsavel pela insercao do aluno no BD
    public void inserirAlunoBD(Aluno aluno, String nomeTabela) {
        TABELA = nomeTabela;
        ContentValues values = new ContentValues();
        values.put("matricula", aluno.getMatricula());
        values.put("nome", aluno.getNome());
        values.put("bluetoothAddress", aluno.getBluetoothAddress());
        values.put("frequencia", aluno.getFrequencia());
        values.put("curso", aluno.getCurso());
        values.put("email", aluno.getEmail());
        bancoDeDados.insert(TABELA, null, values);
    }

    // Metodo responsavel pela remocao do aluno no BD
    public void removerAlunoBD(String matriculaAluno, String nomeTabela) {
        TABELA = nomeTabela;
        bancoDeDados.delete(TABELA, "matricula = " + matriculaAluno, null);
    }

    // Metodo responsavel pela listagem dos alunos presentes no BD
    public List<Aluno> listarAlunoBD(String nomeTurma) {
        TABELA = nomeTurma;
        List<Aluno> lista = new ArrayList<Aluno>();
        String query = "SELECT * FROM " + TABELA;
        Cursor cursor = bancoDeDados.rawQuery(query, null);

        try{
            while(cursor.moveToNext()) {
                Aluno aluno = new Aluno();
                aluno.setNome(cursor.getString(1));
                aluno.setMatricula(cursor.getInt(2));
                aluno.setBluetoothAddress(cursor.getString(3));
                aluno.setFrequencia(cursor.getString(4));
                aluno.setCurso(cursor.getString(5));
                aluno.setEmail(cursor.getString(6));
                lista.add(aluno);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return lista;
    }

    // Metodo responsavel pela verificacao da existencia do aluno no BD
    public boolean isAlunoBD(String matriculaAluno, String nomeTabela) {
        TABELA = nomeTabela;
        String query = "SELECT * FROM " + TABELA;
        Cursor cursor = bancoDeDados.rawQuery(query, null);

        try{
            while(cursor.moveToNext()) {
                if(matriculaAluno.equals(cursor.getInt(2) + "")) { // Verifica pela matricula do aluno
                    cursor.close();
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        cursor.close();
        return false;
    }

    public void inserirFrequencia(String nomeTabela, String novaFrequencia, int id){
        TABELA = nomeTabela;
        String query = "UPDATE " + nomeTabela + " SET frequencia = '" + novaFrequencia + "' WHERE id = '" + id + "'";
        bancoDeDados.execSQL(query);
    }


    public void inserirBluetoothAddressBD(Aluno aluno, int id, String nomeTabela){
        TABELA = nomeTabela;
        String query = "UPDATE " + TABELA + " SET bluetoothAddress = '" + aluno.getBluetoothAddress() + "' " + "WHERE id = '" + id + "'";
        bancoDeDados.execSQL(query);
    }

    //###################################### FIM DOS METODOS RESPONSÁVEIS PELA CLASSE ALUNO ###########################################################
    //#################################################################################################################################################



    //##################################### METODOS RESPONSÁVEIS PELA CLASSE REGISTRO ################################################################
    //################################################################################################################################################

    // Metodo responsavel pela criacao da tabela contendo os dados das chamadas realizadas
    public void criarTabelaRegistro(String nomeTabela){
        TABELA = nomeTabela + "Registro";
        String query = "CREATE TABLE IF NOT EXISTS " + TABELA + " (id INTEGER PRIMARY KEY AUTOINCREMENT, data TEXT, presenca INTEGER, ementa TEXT, inicial INTEGER, final INTEGER)";
        bancoDeDados.execSQL(query);
    }

    // Metodo responsavel pela insercao do Registro no BD
    public void inserirRegistroBD(Registro registro, String nomeTabela){
        TABELA = nomeTabela + "Registro";

        ContentValues values = new ContentValues();
        values.put("data", registro.getData());
        values.put("presenca", registro.getPresencas());
        values.put("ementa", registro.getEmenta());
        values.put("inicial", registro.getPosicaoInicial());
        values.put("final", registro.getPosicaoFinal());
        bancoDeDados.insert(TABELA, null, values);
    }

    public void sobrescreverRegistroBD(Registro registro, int id, String nomeTabela){
        TABELA = nomeTabela + "Registro";
        String query = "UPDATE " + TABELA +
                " SET data = '" + registro.getData() + "', " +
                "presenca = '" + registro.getPresencas() + "', " +
                "ementa = '" + registro.getEmenta() + "', " +
                "inicial = '" + registro.getPosicaoInicial() + "', " +
                "final = '" + registro.getPosicaoFinal() + "' " +
                "WHERE id = '" + id + "'";
        bancoDeDados.execSQL(query);
    }

    // Metodo responsavel pela listagem dos registros de chamada da turma
    public List<Registro> listarRegistroBD(String nomeTurma){
        TABELA = nomeTurma + "Registro";
        List<Registro> lista = new ArrayList<Registro>();
        String query = "SELECT * FROM "+ TABELA;
        Cursor cursor = bancoDeDados.rawQuery(query, null);

        try{
            while(cursor.moveToNext()){
                Registro registro = new Registro();
                registro.setData(cursor.getString(1));
                registro.setPresencas(cursor.getInt(2));
                registro.setEmenta(cursor.getString(3));
                registro.setPosicaoInicial(cursor.getInt(4));
                registro.setPosicaoFinal(cursor.getInt(5));
                lista.add(registro);
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return lista;
    }

    // Metodo responsavel pela verificacao da existencia do registro no BD
    public boolean isRegistroBD(String data, String nomeTabela){
        TABELA = nomeTabela + "Registro";
        String query = "SELECT * FROM " + TABELA;
        Cursor cursor = bancoDeDados.rawQuery(query, null);

        try{
            while(cursor.moveToNext()){
                if(data.equals(cursor.getString(1))){
                    cursor.close();
                    return true;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        cursor.close();
        return false;
    }

    //###################################### FIM DOS METODOS RESPONSÁVEIS PELA CLASSE REGISTRO ########################################################
    //#################################################################################################################################################

    // Metodo responsavel pela limpeza do BD
    public void limparBD(ArrayList<String> tabelas) {
        String query;
        for(int i=0; i<tabelas.size(); i++) {
            query = "DROP TABLE IF EXISTS " + tabelas.get(i).toString();
            bancoDeDados.execSQL(query);
            query = query + "Registro";
            bancoDeDados.execSQL(query);
        }
        query = "DELETE FROM " + TABELATURMAS;
        bancoDeDados.execSQL(query);
    }
}
