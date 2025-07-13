package io.github.artemget.entrys.profile;

import io.github.artemget.entrys.EntryException;
import io.github.artemget.entrys.fake.EFakeErr;
import io.github.artemget.entrys.file.EVal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

final class EValProfTest {

    @Test
    void throwsAtEmptyProfile(){
        Assertions.assertThrows(
                EntryException.class,
                ()-> new EValProf("profile", new EFakeErr<>().toString()).value(),
                "Didnt throw at error getting content"
        );
    }
}
