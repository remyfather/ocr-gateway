import React from 'react';
import { Layout as RaLayout, LayoutProps } from 'react-admin';
import { AppBar, TitlePortal } from 'react-admin';

const CustomAppBar = () => (
  <AppBar>
    <TitlePortal />
  </AppBar>
);

export const Layout: React.FC<LayoutProps> = (props) => (
  <RaLayout {...props} appBar={CustomAppBar} />
); 