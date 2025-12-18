package pt.psoft.g1.psoftg1.api.views;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserView {

	private Long id;
	private String username;
	private String fullName;
}
