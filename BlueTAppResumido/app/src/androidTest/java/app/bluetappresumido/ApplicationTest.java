package app.bluetappresumido;

import app.bluetappresumido.Activities.Main;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;

import com.robotium.solo.Solo;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ActivityInstrumentationTestCase2 {
    private Solo solo;

    public ApplicationTest() {
        super(Main.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
    }

    public void test_main() throws Exception{
        solo.unlockScreen();
        Button btn_test = (Button) solo.getView(R.id.bt_inserir_turma);
        solo.clickOnView(btn_test);
    }
}


