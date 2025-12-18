package pt.psoft.g1.psoftg1.shared.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

@Getter
@AllArgsConstructor
public class ListResponse<T> {
    private final List<T> data;
}
