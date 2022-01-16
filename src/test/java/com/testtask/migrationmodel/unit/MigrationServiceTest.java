package com.testtask.migrationmodel.unit;

import com.testtask.migrationmodel.entity.*;
import com.testtask.migrationmodel.repository.*;
import com.testtask.migrationmodel.service.MigrationService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.times;

@SpringBootTest
public class MigrationServiceTest {
    @Autowired
    private MigrationService migrationService;
    @MockBean
    private MigrationRepository migrationRepository;
    @MockBean
    private WorkloadRepository workloadRepository;
    @MockBean
    private TargetCloudRepository targetCloudRepository;
    @MockBean
    private VolumeRepository volumeRepository;
    @MockBean
    private CredentialsRepository credentialsRepository;

    @Captor
    private ArgumentCaptor<Workload> workloadArgumentCaptor;
    @Captor
    private ArgumentCaptor<Migration> migrationArgumentCaptor;

    private void assertMigration(Migration original, Migration compare) {
        Assert.assertEquals(original.getId(), compare.getId());
        Assert.assertEquals(original.getMigrationState(), compare.getMigrationState());
        Assert.assertEquals(original.getSourceId(), compare.getSourceId());
        Assert.assertEquals(original.getTargetCloudId(), compare.getTargetCloudId());
        Assert.assertEquals(original.getMountPoints(), compare.getMountPoints());
    }

    @Test
    public void validateFound() {
        var migration = new Migration(0L, List.of("qwe"), 1L, 2L, MigrationState.notStarted);
        var migrationToReturn = Optional.of(migration);

        Mockito.doReturn(migrationToReturn)
                .when(migrationRepository)
                .findById(0L);

        var _migration = migrationService.validate(0L);
        assertMigration(migration, _migration);
    }

    @Test
    public void validateNotFound() {
        Mockito.doReturn(Optional.empty())
                .when(migrationRepository)
                .findById(0L);

        try {
            var _migration = migrationService.validate(0L);
        } catch (Exception e) {
            Assert.assertEquals("Can not find Migration with id 0", e.getMessage());
            return;
        }

        Assert.fail();
    }

    @Test
    public void runTest() {
        var migration = new Migration(0L, List.of("C"), 1L, 2L, MigrationState.notStarted);
        var workload = new Workload(1L, "1.2.3", 3L, List.of(4L, 10L));
        var workloadTarget = new Workload(5L, "3.2.1", 3L, List.of());
        var targetCloud = new TargetCloud(2L, CloudType.aws, 3L, 5L);
        var volume = new Volume(4L, "C", 1);
        var volume2 = new Volume(10L, "D", 2);
        var credentials = new Credentials(3L, "name", "pass", "domain");

        Mockito.doReturn(Optional.of(migration)).when(migrationRepository).findById(0L);
        Mockito.doReturn(Optional.of(workload)).when(workloadRepository).findById(1L);
        Mockito.doReturn(Optional.of(workloadTarget)).when(workloadRepository).findById(5L);
        Mockito.doReturn(Optional.of(targetCloud)).when(targetCloudRepository).findById(2L);
        Mockito.doReturn(Optional.of(volume)).when(volumeRepository).findById(4L);
        Mockito.doReturn(Optional.of(volume2)).when(volumeRepository).findById(10L);
        Mockito.doReturn(Optional.of(credentials)).when(credentialsRepository).findById(3L);

        Assert.assertEquals(MigrationState.notStarted, migration.getMigrationState());

        long startTime = System.currentTimeMillis();

        migrationService.run(migration);

        long time = System.currentTimeMillis() - startTime;
        long timeMinutes = time / 1000;
        //time difference is less than 10%
        double difference = 5f * 60 / timeMinutes;

        //Assert.assertEquals(1, difference, 0.1f);

        Mockito.verify(workloadRepository).save(workloadArgumentCaptor.capture());
        var workloadAnswer = workloadArgumentCaptor.getValue();

        Assert.assertEquals(workloadTarget.getIp(), workloadAnswer.getIp());
        Assert.assertEquals(workloadTarget.getCredentialsId(), workloadAnswer.getCredentialsId());
        Assert.assertEquals(workloadAnswer.getIp(), workloadAnswer.getIp());
        Assert.assertEquals(List.of(4L), workloadAnswer.getVolumeIds());

        Mockito.verify(migrationRepository, times(2)).save(migrationArgumentCaptor.capture());
        var migrationAnswers = migrationArgumentCaptor.getAllValues();
        Assert.assertEquals(MigrationState.success, migrationAnswers.get(1).getMigrationState());
    }

    //TODO: more run tests to non exist parts

    @Test
    public void addTest() {
        var migration = new Migration(null, List.of("C"), 1L, 2L, MigrationState.notStarted);
        var workload = new Workload(1L, "1.2.3", 3L, List.of(4L, 10L));
        var targetCloud = new TargetCloud(2L, CloudType.aws, 3L, 5L);

        Mockito.doReturn(Optional.of(migration)).when(migrationRepository).findById(0L);
        Mockito.doReturn(Optional.of(workload)).when(workloadRepository).findById(1L);
        Mockito.doReturn(Optional.of(targetCloud)).when(targetCloudRepository).findById(2L);

        Mockito.doReturn(null).when(migrationRepository).getLastId();

        var _migration = migrationService.add(migration);

        migration.setId(0L);

        Mockito.verify(migrationRepository).save(migrationArgumentCaptor.capture());
        var migrationAnswer = migrationArgumentCaptor.getValue();

        assertMigration(migration, _migration);
        assertMigration(migration, migrationAnswer);
    }

    @Test
    public void modifyTest() {
        var migration = new Migration(0L, List.of("C"), 1L, 2L, MigrationState.notStarted);
        var migrationModify = new Migration(0L, List.of("D"), 2L, 2L, MigrationState.error);

        Mockito.doReturn(Optional.of(migration)).when(migrationRepository).findById(0L);

        var _migration = migrationService.modify(0L, migrationModify);

        Mockito.verify(migrationRepository).save(migrationArgumentCaptor.capture());
        var migrationAnswer = migrationArgumentCaptor.getValue();

        assertMigration(migrationModify, _migration);
        assertMigration(migration, migrationAnswer);
    }
}
