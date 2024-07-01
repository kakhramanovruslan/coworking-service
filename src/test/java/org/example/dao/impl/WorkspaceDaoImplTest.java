package org.example.dao.impl;

import org.example.config.ContainersEnvironment;
import org.example.entity.Workspace;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

public class WorkspaceDaoImplTest extends ContainersEnvironment {

    WorkspaceDaoImpl workspaceDao;
    private Workspace testWorkspace;

    @BeforeEach
    public void setUp(){
        workspaceDao = WorkspaceDaoImpl.getInstance();

        workspaceDao.deleteAll();

        testWorkspace = testWorkspace.builder()
                .name("test-workspace-1")
                .build();
        workspaceDao.save(testWorkspace);
    }

    @AfterEach
    public void reset(){
        workspaceDao.deleteAll();
    }

    @Test
    public void testFindAll(){
        Workspace testWorkspace2 = Workspace.builder()
                .name("test-workspace-2")
                .build();

        workspaceDao.save(testWorkspace2);

        assertAll(
            () -> assertThat(workspaceDao.findAll()).hasSize(2),
            () -> assertThat(workspaceDao.findAll().isEmpty()).isFalse()
        );
    }

    @Test
    public void testFindById(){
        Optional<Workspace> foundWorkspace = workspaceDao.findById(testWorkspace.getId());
        Optional<Workspace> notFoundWorkspace = workspaceDao.findById(999L);
        assertAll(
                () -> assertTrue(foundWorkspace.isPresent()),
                () -> assertThat(testWorkspace.getId()).isEqualTo(foundWorkspace.get().getId()),
                () -> assertThat(testWorkspace.getName()).isEqualTo(foundWorkspace.get().getName()),
                () -> assertFalse(notFoundWorkspace.isPresent())
        );
    }

    @Test
    public void testDeleteById(){
        assertAll(
                () -> assertThat(workspaceDao.deleteById(testWorkspace.getId())).isTrue(),
                () -> assertThat(workspaceDao.findById(testWorkspace.getId()).isEmpty()).isTrue()
        );
    }

    @Test
    public void testDeleteAll(){
        Workspace testWorkspace2 = Workspace.builder()
                .name("test-workspace-2")
                .build();
        workspaceDao.save(testWorkspace2);

        assertAll(
                () -> assertThat(workspaceDao.deleteAll()).isTrue(),
                () -> assertThat(workspaceDao.findAll().size()).isEqualTo(0)
        );
    }

    @Test
    public void testSave(){
        Workspace testWorkspace2 = Workspace.builder()
                .name("test-workspace-2")
                .build();
        Workspace savedWorkspace = workspaceDao.save(testWorkspace2);

        assertAll(
                () -> assertThat(workspaceDao.findById(savedWorkspace.getId()).isPresent()).isTrue(),
                () -> assertThat(savedWorkspace.getId()).isEqualTo(testWorkspace2.getId()),
                () -> assertThat(savedWorkspace.getName()).isEqualTo(testWorkspace2.getName()),
                () -> assertThat(workspaceDao.findAll().size()).isEqualTo(2)
        );
    }

    @Test
    public void testUpdate(){
        Workspace testWorkspace2= Workspace.builder()
                .name("test-workspace-2")
                .build();
        Workspace testWorkspaceUpdate = workspaceDao.save(testWorkspace2);

        testWorkspaceUpdate.setName("test-workspace-update");
        workspaceDao.update(testWorkspaceUpdate);

        assertAll(
                () -> assertThat(workspaceDao.findById(testWorkspaceUpdate.getId())).isPresent(),
                () -> assertThat(workspaceDao.findById(testWorkspaceUpdate.getId()).get().getName()).isEqualTo(testWorkspaceUpdate.getName())
        );
    }

    @Test
    public void testFindByName(){
        Optional<Workspace> foundWorkspace = workspaceDao.findByName(testWorkspace.getName());
        Optional<Workspace> notFoundWorkspace = workspaceDao.findByName(" ");

        assertAll(
                () -> assertThat(foundWorkspace.isPresent()).isTrue(),
                () -> assertThat(testWorkspace.getId()).isEqualTo(foundWorkspace.get().getId()),
                () -> assertThat(testWorkspace.getName()).isEqualTo(foundWorkspace.get().getName()),
                () -> assertThat(notFoundWorkspace.isPresent()).isFalse()
        );
    }

    @Test
    public void testDeleteByName(){
        assertAll(
                () -> assertThat(workspaceDao.deleteByName(testWorkspace.getName())).isTrue(),
                () -> assertThat(workspaceDao.findByName(testWorkspace.getName()).isEmpty()).isTrue()
        );
    }


}
