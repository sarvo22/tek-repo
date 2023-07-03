package com.tekfilo.inventory.shape;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

public class ShapeServiceTest {


    List<ShapeEntity> shapeEntityList = new ArrayList<>();
    private ShapeRepository shapeRepository = Mockito.mock(ShapeRepository.class);

    @Before
    public void init() {
        shapeEntityList.add(getShapes());
        shapeEntityList.add(getShapes());
        shapeEntityList.add(getShapes());
    }

    private ShapeEntity getShapes() {
        ShapeEntity shape = new ShapeEntity();
        shape.setShapeName("Test");
        shape.setIsDeleted(0);
        shape.setId(100);
        shape.setIsLocked(0);
        shape.setCompanyId(1000);
        return shape;
    }

    @Test
    void saveShapeTest() throws Exception {
        ShapeService shapeService = new ShapeService(shapeRepository);

        ShapeDto dto = new ShapeDto();
        dto.setId(200);
        dto.setShapeName("Save");

        ShapeEntity entity = new ShapeEntity();
        entity.setId(200);
        entity.setShapeName("Save");

        Mockito.when(shapeRepository.save(any(ShapeEntity.class))).thenReturn(entity);

        ShapeEntity actual = shapeService.save(dto);

        Assert.assertEquals(actual.getId(), entity.getId());
        //Assertions.assertThat(entity.getShapeName()).isEqualTo(actual.getShapeName());

    }

    @Test
    void findAllShapeTest() {
    }

    @Test
    @DisplayName("Find Shape by provided ID")
    void findByIdTest() {
        ShapeService shapeService = new ShapeService(shapeRepository);
        ShapeEntity entity = new ShapeEntity();
        entity.setCompanyId(100);
        entity.setId(100);
        entity.setShapeName("Test");

        Mockito.when(shapeRepository.findById(100)).thenReturn(Optional.of(entity));

        ShapeEntity expectedShape = new ShapeEntity();
        expectedShape.setCompanyId(100);
        expectedShape.setId(100);
        expectedShape.setShapeName("Test");

        ShapeEntity actual = shapeService.findById(100);

        Assertions.assertThat(actual.getShapeName()).isEqualTo(expectedShape.getShapeName());
    }

    @Test
    void removeTest() {
    }

    @Test
    void findAllEntitiesByIdsTest() {
    }

    @Test
    void removeAllTest() {
    }

    @Test
    void lockTest() {
    }

    @Test
    void unlockTest() {
    }
}