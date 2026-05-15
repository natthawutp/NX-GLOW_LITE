# GWH_TM_BLDG
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
| 18 | BLDG_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | BLDG_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | BLDG_COD | NVARCHAR2(6) | N | Building Code / 棟コード |
| 21 | BLDG_AREA_COD | CHAR(3) | N | Location Area Code / ロケーションエリアコード |
| 22 | BLDG_NAM | NVARCHAR2(100) | Y | Building Name / 棟名称 |
| 23 | BLDG_TYPE_KND | CHAR(2) | N | Building Type Kind / 棟分類区分 |
| 24 | BLDG_FIPO_NUM | NVARCHAR2(40) | Y | Fire Insurance Policy No. / 火災保険証券番号 |
| 25 | BLDG_FIDT_NUM | NVARCHAR2(6) | Y | Fire Insurance Detail No. / 明細番号 |
| 26 | BLDG_FILT_AMN | NUMBER(14,4) | Y | Insurance Limit / 保険制限額 |
