package com.tekfilo.inventory.color;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ColorServiceTest {

    @InjectMocks
    ColorService colorService;

    @Mock
    ColorRepository colorRepository;

    ColorEntity expected = null;
    ColorDto dto = null;

    @BeforeEach
    public void setUp() {
        expected = new ColorEntity();
        expected.setId(1000);
        expected.setColorName("Test");

        dto = new ColorDto();
        dto.setColorName("Test");
    }

    @Test
    public void save() throws Exception {
        Mockito.when(colorRepository.save(Mockito.any())).thenReturn(expected);
        ColorEntity actual = colorService.save(dto);
        Assert.assertEquals(expected.getColorName(), actual.getColorName());
    }

    @Test
    void findAll() {
    }

    @Test
    void findById() {
    }

    @Test
    void remove() {
    }

    @Test
    void findAllEntitiesByIds() {
    }

    @Test
    void removeAll() {
    }

    @Test
    void lock() {
    }

    @Test
    void unlock() {
    }

    @Test
    void getColorList() {
    }
}