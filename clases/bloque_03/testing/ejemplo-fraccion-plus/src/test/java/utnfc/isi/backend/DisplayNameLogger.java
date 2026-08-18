package utnfc.isi.backend;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

// Logger simple para mostrar en consola el nombre del test que se está ejecutando
//   y si fue exitoso o fallido
//   Solo a efectos didácticos, no es necesario para el funcionamiento de los tests
public class DisplayNameLogger implements TestWatcher, BeforeTestExecutionCallback   {

    @Override
    public void beforeTestExecution(ExtensionContext context) {
        String displayName = context.getDisplayName();
        System.out.println("🔍 Iniciando test: " + displayName);
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        System.out.println("✅ Test exitoso: " + context.getDisplayName());
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        System.out.println("❌ Test fallido: " + context.getDisplayName());
    }
}
