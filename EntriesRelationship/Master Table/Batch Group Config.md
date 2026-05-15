# GWH_TM_BTGC
#Entity #Standard #MASTER

| # | Column Name | Data Type | Nullable | Description |
|---|------------|-----------|----------|-------------|
| 1 | DEL_FLG | CHAR(1) | N | Delete Flag / 論理削除フラグ |
| 2 | CRT_YMD | CHAR(8) | N | Create Date (UTC) / 作成日(標準時) |
| 3 | CRT_TIM | CHAR(6) | N | Create HMS (UTC) / 作成時分秒(標準時) |
| 4 | CRT_TMID | NVARCHAR2(200) | N | Create Term Id / 作成端末ID |
| 5 | CRT_USER | NVARCHAR2(100) | N | Create User Id / 作成ユーザID |
| 6 | CRT_PGM | NVARCHAR2(60) | N | Create Program Id / 作成プログラムID |
| 7 | CRT_TM_ZONE | NVARCHAR2(6) | N | Create User Time Zone / 作成ユーザタイムゾーン |
| 8 | CRT_YMDHMS | TIMESTAMP(6) | N | Create Time Stamp (UTC) / 作成タイムスタンプ（標準時） |
| 9 | CRT_L_YMDHMS | TIMESTAMP(6) | N | Create Time Stamp (Local) / 作成タイムスタンプ（ローカル） |
| 10 | UPD_YMD | CHAR(8) | N | Update Date (UTC) / 更新日(標準時) |
| 11 | UPD_TIM | CHAR(6) | N | Update HMS (UTC) / 更新時分秒(標準時) |
| 12 | UPD_TMID | NVARCHAR2(200) | N | Update Terminal ID / 更新端末ID |
| 13 | UPD_USER | NVARCHAR2(100) | N | Update User ID / 更新ユーザID |
| 14 | UPD_PGM | NVARCHAR2(60) | N | Update Program ID / 更新プログラムID |
| 15 | UPD_TM_ZONE | NVARCHAR2(6) | N | Update User Time Zone / 更新ユーザタイムゾーン |
| 16 | UPD_YMDHMS | TIMESTAMP(6) | N | Update Time Stamp (UTC) / 更新タイムスタンプ（標準時） |
| 17 | UPD_L_YMDHMS | TIMESTAMP(6) | N | Update Time Stamp (Local) / 更新タイムスタンプ（ローカル） |
| 18 | BTGC_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | BTGC_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | BTGC_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | BTGC_WKG_KEY1 | NVARCHAR2(100) | Y | Work Management Group Key1 / 作業管理集約キー1 |
| 22 | BTGC_WKG_KEY2 | NVARCHAR2(100) | Y | Work Management Group Key2 / 作業管理集約キー2 |
| 23 | BTGC_WKG_KEY3 | NVARCHAR2(100) | Y | Work Management Group Key3 / 作業管理集約キー3 |
| 24 | BTGC_WKG_KEY4 | NVARCHAR2(100) | Y | Work Management Group Key4 / 作業管理集約キー4 |
| 25 | BTGC_WKG_KEY5 | NVARCHAR2(100) | Y | Work Management Group Key5 / 作業管理集約キー5 |
| 26 | BTGC_WKG_KEY6 | NVARCHAR2(100) | Y | Work Management Group Key6 / 作業管理集約キー6 |
| 27 | BTGC_WKG_KEY7 | NVARCHAR2(100) | Y | Work Management Group Key7 / 作業管理集約キー7 |
| 28 | BTGC_WKG_KEY8 | NVARCHAR2(100) | Y | Work Management Group Key8 / 作業管理集約キー8 |
| 29 | BTGC_WKG_KEY9 | NVARCHAR2(100) | Y | Work Management Group Key9 / 作業管理集約キー9 |
| 30 | BTGC_WKG_KEY10 | NVARCHAR2(100) | Y | Work Management Group Key10 / 作業管理集約キー10 |
| 31 | BTGC_WKG_KEY11 | NVARCHAR2(100) | Y | Work Management Group Key11 / 作業管理集約キー11 |
| 32 | BTGC_WKG_KEY12 | NVARCHAR2(100) | Y | Work Management Group Key12 / 作業管理集約キー12 |
| 33 | BTGC_WKG_KEY13 | NVARCHAR2(100) | Y | Work Management Group Key13 / 作業管理集約キー13 |
| 34 | BTGC_WKG_KEY14 | NVARCHAR2(100) | Y | Work Management Group Key14 / 作業管理集約キー14 |
| 35 | BTGC_WKG_KEY15 | NVARCHAR2(100) | Y | Work Management Group Key15 / 作業管理集約キー15 |
| 36 | BTGC_WKS_KEY1 | NVARCHAR2(100) | Y | Work Management Sort Key1 / 作業管理ソートキー1 |
| 37 | BTGC_WKS_KEY2 | NVARCHAR2(100) | Y | Work Management Group Key2 / 作業管理ソートキー2 |
| 38 | BTGC_WKS_KEY3 | NVARCHAR2(100) | Y | Work Management Group Key3 / 作業管理ソートキー3 |
| 39 | BTGC_WKS_KEY4 | NVARCHAR2(100) | Y | Work Management Group Key4 / 作業管理ソートキー4 |
| 40 | BTGC_WKS_KEY5 | NVARCHAR2(100) | Y | Work Management Group Key5 / 作業管理ソートキー5 |
| 41 | BTGC_WKS_KEY6 | NVARCHAR2(100) | Y | Work Management Group Key6 / 作業管理ソートキー6 |
| 42 | BTGC_WKS_KEY7 | NVARCHAR2(100) | Y | Work Management Group Key7 / 作業管理ソートキー7 |
| 43 | BTGC_WKS_KEY8 | NVARCHAR2(100) | Y | Work Management Group Key8 / 作業管理ソートキー8 |
| 44 | BTGC_WKS_KEY9 | NVARCHAR2(100) | Y | Work Management Group Key9 / 作業管理ソートキー9 |
| 45 | BTGC_WKS_KEY10 | NVARCHAR2(100) | Y | Work Management Group Key10 / 作業管理ソートキー10 |
| 46 | BTGC_WKS_KEY11 | NVARCHAR2(100) | Y | Work Management Group Key11 / 作業管理ソートキー11 |
| 47 | BTGC_WKS_KEY12 | NVARCHAR2(100) | Y | Work Management Group Key12 / 作業管理ソートキー12 |
| 48 | BTGC_WKS_KEY13 | NVARCHAR2(100) | Y | Work Management Group Key13 / 作業管理ソートキー13 |
| 49 | BTGC_WKS_KEY14 | NVARCHAR2(100) | Y | Work Management Group Key14 / 作業管理ソートキー14 |
| 50 | BTGC_WKS_KEY15 | NVARCHAR2(100) | Y | Work Management Group Key15 / 作業管理ソートキー15 |
| 51 | BTGC_TWKG_KEY1 | NVARCHAR2(100) | Y | Total Work Management Group Key1 / トータル作業管理集約キー1 |
| 52 | BTGC_TWKG_KEY2 | NVARCHAR2(100) | Y | Total Work Management Group Key2 / トータル作業管理集約キー2 |
| 53 | BTGC_TWKG_KEY3 | NVARCHAR2(100) | Y | Total Work Management Group Key3 / トータル作業管理集約キー3 |
| 54 | BTGC_TWKG_KEY4 | NVARCHAR2(100) | Y | Total Work Management Group Key4 / トータル作業管理集約キー4 |
| 55 | BTGC_TWKG_KEY5 | NVARCHAR2(100) | Y | Total Work Management Group Key5 / トータル作業管理集約キー5 |
| 56 | BTGC_TWKG_KEY6 | NVARCHAR2(100) | Y | Total Work Management Group Key6 / トータル作業管理集約キー6 |
| 57 | BTGC_TWKG_KEY7 | NVARCHAR2(100) | Y | Total Work Management Group Key7 / トータル作業管理集約キー7 |
| 58 | BTGC_TWKG_KEY8 | NVARCHAR2(100) | Y | Total Work Management Group Key8 / トータル作業管理集約キー8 |
| 59 | BTGC_TWKG_KEY9 | NVARCHAR2(100) | Y | Total Work Management Group Key9 / トータル作業管理集約キー9 |
| 60 | BTGC_TWKG_KEY10 | NVARCHAR2(100) | Y | Total Work Management Group Key10 / トータル作業管理集約キー10 |
| 61 | BTGC_TWKG_KEY11 | NVARCHAR2(100) | Y | Total Work Management Group Key11 / トータル作業管理集約キー11 |
| 62 | BTGC_TWKG_KEY12 | NVARCHAR2(100) | Y | Total Work Management Group Key12 / トータル作業管理集約キー12 |
| 63 | BTGC_TWKG_KEY13 | NVARCHAR2(100) | Y | Total Work Management Group Key13 / トータル作業管理集約キー13 |
| 64 | BTGC_TWKG_KEY14 | NVARCHAR2(100) | Y | Total Work Management Group Key14 / トータル作業管理集約キー14 |
| 65 | BTGC_TWKG_KEY15 | NVARCHAR2(100) | Y | Total Work Management Group Key15 / トータル作業管理集約キー15 |
| 66 | BTGC_TWKS_KEY1 | NVARCHAR2(100) | Y | Total Work Management Sort Key1 / トータル作業管理ソートキー1 |
| 67 | BTGC_TWKS_KEY2 | NVARCHAR2(100) | Y | Total Work Management Sort Key2 / トータル作業管理ソートキー2 |
| 68 | BTGC_TWKS_KEY3 | NVARCHAR2(100) | Y | Total Work Management Sort Key3 / トータル作業管理ソートキー3 |
| 69 | BTGC_TWKS_KEY4 | NVARCHAR2(100) | Y | Total Work Management Sort Key4 / トータル作業管理ソートキー4 |
| 70 | BTGC_TWKS_KEY5 | NVARCHAR2(100) | Y | Total Work Management Sort Key5 / トータル作業管理ソートキー5 |
| 71 | BTGC_TWKS_KEY6 | NVARCHAR2(100) | Y | Total Work Management Sort Key6 / トータル作業管理ソートキー6 |
| 72 | BTGC_TWKS_KEY7 | NVARCHAR2(100) | Y | Total Work Management Sort Key7 / トータル作業管理ソートキー7 |
| 73 | BTGC_TWKS_KEY8 | NVARCHAR2(100) | Y | Total Work Management Sort Key8 / トータル作業管理ソートキー8 |
| 74 | BTGC_TWKS_KEY9 | NVARCHAR2(100) | Y | Total Work Management Sort Key9 / トータル作業管理ソートキー9 |
| 75 | BTGC_TWKS_KEY10 | NVARCHAR2(100) | Y | Total Work Management Sort Key10 / トータル作業管理ソートキー10 |
| 76 | BTGC_TWKS_KEY11 | NVARCHAR2(100) | Y | Total Work Management Sort Key11 / トータル作業管理ソートキー11 |
| 77 | BTGC_TWKS_KEY12 | NVARCHAR2(100) | Y | Total Work Management Sort Key12 / トータル作業管理ソートキー12 |
| 78 | BTGC_TWKS_KEY13 | NVARCHAR2(100) | Y | Total Work Management Sort Key13 / トータル作業管理ソートキー13 |
| 79 | BTGC_TWKS_KEY14 | NVARCHAR2(100) | Y | Total Work Management Sort Key14 / トータル作業管理ソートキー14 |
| 80 | BTGC_TWKS_KEY15 | NVARCHAR2(100) | Y | Total Work Management Sort Key15 / トータル作業管理ソートキー15 |
