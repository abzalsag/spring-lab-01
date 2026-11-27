package kz.iitu.springlab.catalog;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock BookRepository repository;
    @InjectMocks BookService service;

    // 1. Happy path
    @Test
    void authorFilterIgnoresCase() {
        given(repository.findAll()).willReturn(List.of(
                new Book(1L, "Dune", "Frank Herbert", 1965),
                new Book(2L, "Clean Code", "Robert Martin", 2008)));

        assertThat(service.findAll("HERBERT"))
                .extracting(Book::title)
                .containsExactly("Dune");
    }

    // 2. Not-found path
    @Test
    void replacingMissingBookReturnsEmptyAndSavesNothing() {
        given(repository.findById(99L)).willReturn(Optional.empty());

        var result = service.replace(99L, new Book(null, "X", "Y", 2000));

        assertThat(result).isEmpty();
        verify(repository, never()).save(any());
    }

    // 3. Interaction check with ArgumentCaptor
    @Test
    void createIgnoresIdSentByClient() {
        given(repository.save(any(Book.class))).willAnswer(inv -> inv.getArgument(0));

        service.create(new Book(42L, "Dune", "Frank Herbert", 1965));

        var captor = ArgumentCaptor.forClass(Book.class);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue().id()).isNull();
    }

    // 4. The branch that is easy to forget: blank author
    @Test
    void blankAuthorReturnsEveryBook() {
        var all = List.of(new Book(1L, "Dune", "Frank Herbert", 1965));
        given(repository.findAll()).willReturn(all);

        assertThat(service.findAll("   ")).isEqualTo(all);
    }
}
