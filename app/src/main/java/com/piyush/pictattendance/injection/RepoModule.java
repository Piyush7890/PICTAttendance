package com.piyush.pictattendance.injection;


import com.piyush.pictattendance.data.LoginRepo;
import com.piyush.pictattendance.data.LoginRepoImpl;
import com.piyush.pictattendance.data.Repository;
import com.piyush.pictattendance.data.RepositoryImpl;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class RepoModule {
    @Binds
    abstract Repository provideRepository(RepositoryImpl repository);

    @Binds
    abstract LoginRepo provideLoginRepo(LoginRepoImpl loginRepo);
}
