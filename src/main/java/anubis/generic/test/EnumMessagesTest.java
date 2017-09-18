package anubis.generic.test;

import anubis.MessageSystem;

public enum EnumMessagesTest {

	CREATE {
		@Override
		public String getMessage(Boolean isMasculino, String attributeUniqueField, String valueAttributeUniqueField, String nomeController) {
			return MessageSystem.formatMessage(isMasculino ? "crud.generic.cadastrado.sucesso.masculino" : "crud.generic.cadastrado.sucesso.feminino", nomeController);
		}
	},
	EDIT {
		@Override
		public String getMessage(Boolean isMasculino, String attributeUniqueField, String valueAttributeUniqueField, String nomeController) {
			return MessageSystem.formatMessage(isMasculino ? "crud.generic.alterado.sucesso.masculino" : "crud.generic.alterado.sucesso.feminino", nomeController);
		}
	},
	DELETE {
		@Override
		public String getMessage(Boolean isMasculino, String attributeUniqueField, String valueAttributeUniqueField, String nomeController) {
			return MessageSystem.formatMessage(isMasculino ? "crud.generic.excluido.sucesso.masculino" : "crud.generic.excluido.sucesso.feminino", nomeController);
		}
	},
	DELETE_WITH_DEPENDENCIES {
		@Override
		public String getMessage(Boolean isMasculino, String attributeUniqueField, String valueAttributeUniqueField, String nomeController) {
			return MessageSystem.formatMessage(isMasculino ? "crud.generic.erro.excluir.masculino" : "crud.generic.erro.excluir.feminino", nomeController.toLowerCase());
		}
	},
	GET_EDIT_DELETE_NOT_FOUND {
		@Override
		public String getMessage(Boolean isMasculino, String attributeUniqueField, String valueAttributeUniqueField, String nomeController) {
			return MessageSystem.formatMessage(isMasculino ? "crud.generic.nao.encontrado.masculino" : "crud.generic.nao.encontrado.feminino", nomeController);
		}
	},
	CREATE_EDIT_UNIQUE_FIELD_EXIST {
		@Override
		public String getMessage(Boolean isMasculino, String attributeUniqueField, String valueAttributeUniqueField, String nomeController) {
			return MessageSystem.formatMessage(isMasculino ? "crud.generic.exist.unique.masculino" : "crud.generic.exist.unique.feminino", attributeUniqueField, valueAttributeUniqueField, nomeController);
		}
	},
	CREATE_EDIT_FIELD_NAME_EMPTY {
		@Override
		public String getMessage(Boolean isMasculino, String attributeUniqueField, String valueAttributeUniqueField, String nomeController) {
			return MessageSystem.formatMessage("crud.validation.required#{label.nome}");
		}
	},
	CREATE_EDIT_FIELD_ACTIVE_EMPTY {
		@Override
		public String getMessage(Boolean isMasculino, String attributeUniqueField, String valueAttributeUniqueField, String nomeController) {
			return MessageSystem.formatMessage("crud.validation.required#{label.is.ativo}");
		}
	};
	
	public abstract String getMessage(Boolean isMasculino, String attributeUniqueField, String valueAttributeUniqueField, String nomeController);
	
	private EnumMessagesTest() {}
	
}
