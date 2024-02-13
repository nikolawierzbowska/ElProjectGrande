package pl.elgrandeproject.elgrande.entities.user.validation;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pl.elgrandeproject.elgrande.entities.user.UserClass;
import pl.elgrandeproject.elgrande.entities.user.UserRepository;

import java.util.Optional;

class UnoccupiedValidatorTest {

    private final UserRepository userRepository = Mockito.mock(UserRepository.class);

    private final UnoccupiedValidator testUnoccupiedValidator= new UnoccupiedValidator(userRepository);

    @Test
    void shouldReturnTrueIfEmailIsNotValid() {
        //given:
        Mockito.when(userRepository.findByEmail(Mockito.anyString()))
                .thenReturn(Optional.empty());

        //when:
        boolean actual = testUnoccupiedValidator.isValid("test-email", null);

        //then:
        Assertions.assertThat(actual).isTrue();

    }

    @Test
    void shouldThrowEmailInUseExceptionIfEmailIsTaken() {
        //given:
        Mockito.when(userRepository.findByEmail("test-email"))
                .thenReturn(Optional.of(new UserClass()));

        //when:
        Throwable throwable = Assertions.catchThrowable(
                () -> testUnoccupiedValidator.isValid("test-email",null));

        //then:
        Assertions.assertThat(throwable).isInstanceOf(EmailInUseException.class);
        Assertions.assertThat(throwable.getMessage()).isEqualTo("test-email is already in use");

    }

}