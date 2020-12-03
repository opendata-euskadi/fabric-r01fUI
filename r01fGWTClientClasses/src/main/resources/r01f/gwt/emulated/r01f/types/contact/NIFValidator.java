package r01f.types.contact;

import r01f.validation.ObjectValidationResult;
import r01f.validation.ObjectValidationResultBuilder;
import r01f.validation.Validates;

public class NIFValidator
  implements Validates<PersonID> {
/////////////////////////////////////////////////////////////////////////////////////////
//  
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public ObjectValidationResult<PersonID> validate(final PersonID nif) {
		boolean nifValid = _validateNif(nif);
		return nifValid ? ObjectValidationResultBuilder.on(nif)
													   .isValid()
						: ObjectValidationResultBuilder.on(nif)
													   .isNotValidBecause("Not valid nif={}",nif);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  
/////////////////////////////////////////////////////////////////////////////////////////	
	public boolean _validateNif(final PersonID nif) {
		String nifStr = nif.asString();

		String letraMayuscula = ""; // Guardar la letra introducida en formato mayscula

		// Aqu excluimos cadenas distintas a 9 caracteres que debe tener un dni y
		// tambin si el ltimo caracter no es una letra
		if (nifStr.length() != 9 || Character.isLetter(nifStr.charAt(8)) == false) {
			return false;
		}

		// Al superar la primera restriccin, la letra la pasamos a mayscula
		letraMayuscula = (nifStr.substring(8)).toUpperCase();

		// Por ltimo validamos que slo tengo 8 dgitos entre los 8 primeros caracteres
		// y que la letra introducida es igual a la de la ecuacin
		// Llamamos a los mtodos privados de la clase soloNumeros() y letraDNI()
		if (_onlyNumbers(nifStr) == true && _dniLetter(nifStr).equals(letraMayuscula)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean _onlyNumbers(final String nifStr) {
		int i, j = 0;
		String numero = ""; // Es el nmero que se comprueba uno a uno por si hay alguna letra entre los 8
							// primeros dgitos
		String miDNI = ""; // Guardamos en una cadena los nmeros para despus calcular la letra
		String[] unoNueve = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };

		for (i = 0; i < nifStr.length() - 1; i++) {
			numero = nifStr.substring(i, i + 1);

			for (j = 0; j < unoNueve.length; j++) {
				if (numero.equals(unoNueve[j])) {
					miDNI += unoNueve[j];
				}
			}
		}

		if (miDNI.length() != 8) {
			return false;
		} else {
			return true;
		}
	}
	private String _dniLetter(final String nifStr) {
		// El mtodo es privado porque lo voy a usar internamente en esta clase, no se
		// necesita fuera de ella

		// pasar miNumero a integer
		int miDNI = Integer.parseInt(nifStr.substring(0, 8));
		int resto = 0;
		String miLetra = "";
		String[] asignacionLetra = { "T", "R", "W", "A", "G", "M", "Y", "F", "P", "D", "X", "B", "N", "J", "Z", "S","Q", "V", "H", "L", "C", "K", "E" };

		resto = miDNI % 23;
		miLetra = asignacionLetra[resto];
		return miLetra;
	}
}
