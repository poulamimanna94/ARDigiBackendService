package com.siemens.serviceImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.siemens.domain.Asset;
import com.siemens.domain.Header;
import com.siemens.domain.Unit;

/**
 * Service for Reading and passing .xlsx file data.
 */
@Service
public class FileReaderService
{
    
    @Autowired
    private UnitServiceImpl unitServiceImpl;
    
    @Autowired
    private SectionServiceImpl sectionServiceImpl;
    
    @Autowired
    private AssetServiceImpl assetServiceImpl;
    
    public Unit makeAndSaveUnit(String unitName)
    {
        Unit unit = new Unit();
        unit.setUnitName(unitName);
        return unitServiceImpl.save(unit);
    }
    
    public Header makeAndSaveSection(String sectionName, Unit savedUnit)
    {
        Header section = new Header();
        section.setSectionName(sectionName);
        section.setUnit(savedUnit);
        return sectionServiceImpl.save(section, savedUnit.getId());
        
    }
    
    public void makeAndSaveAsset(String type, String x, String y, String z, Header savedSection, String assetsKKSTags)
    {
		/*
		 * Asset asset = new Asset(); asset.setSection(savedSection); asset.setX(x);
		 * asset.setY(y); asset.setZ(z); asset.setElevation(z);
		 * asset.setAssetType(type); asset.setKksTag(assetsKKSTags);
		 * assetServiceImpl.save(asset, savedSection.getId());
		 */
    }
    
    public void makeAndUpdateAsset(String type, String x, String y, String z, Header savedSection, String assetsKKSTags, Asset savedAsset)
    {
		/*
		 * Asset asset = new Asset(); asset.setSection(savedSection); asset.setX(x);
		 * asset.setY(y); asset.setZ(z); asset.setElevation(z);
		 * asset.setAssetType(type); asset.setKksTag(assetsKKSTags);
		 * asset.setId(savedAsset.getId()); assetServiceImpl.partialUpdate(asset);
		 */
    }
    
    @SuppressWarnings("deprecation")
    public String getStringFromCellValues(Iterator<Cell> cellIterator)
    {
        Cell cell = cellIterator.next();
        switch (cell.getCellType())
        {
            case Cell.CELL_TYPE_STRING:
                return cell.getStringCellValue();
            case Cell.CELL_TYPE_NUMERIC: // field that represents number cell type
                return String.valueOf(cell.getNumericCellValue());
            default:
                return "";
        }
    }
    
    public void getSheetFromExcel(String filePath) throws FileNotFoundException, IOException
    {
		/*
		 * FileInputStream file = new FileInputStream(new File(filePath)); Workbook
		 * workbook = new XSSFWorkbook(file); Sheet sheet = workbook.getSheetAt(0);
		 * Iterator<Row> itr = sheet.iterator(); for (int skipRow = 0; skipRow < 3;
		 * skipRow++) { itr.next(); } while (itr.hasNext()) { Row row = itr.next();
		 * Iterator<Cell> cellIterator = row.cellIterator(); // iterating over each
		 * column
		 * 
		 * while (cellIterator.hasNext()) { String unitName =
		 * getStringFromCellValues(cellIterator); String sectionName =
		 * getStringFromCellValues(cellIterator);
		 * 
		 * @SuppressWarnings("unused") String assetName =
		 * getStringFromCellValues(cellIterator); String assetsKKSTags =
		 * getStringFromCellValues(cellIterator);
		 * 
		 * @SuppressWarnings("unused") String type =
		 * getStringFromCellValues(cellIterator);
		 * 
		 * @SuppressWarnings("unused") String building =
		 * getStringFromCellValues(cellIterator); String x =
		 * getStringFromCellValues(cellIterator); String y =
		 * getStringFromCellValues(cellIterator); String z =
		 * getStringFromCellValues(cellIterator); Optional<Unit> unitData =
		 * unitServiceImpl.findByName(unitName); if (unitData.isEmpty()) { Unit
		 * savedUnit = makeAndSaveUnit(unitName); Section savedSection =
		 * makeAndSaveSection(sectionName, savedUnit); makeAndSaveAsset(type, x, y, z,
		 * savedSection, assetsKKSTags); break; } Optional<Section> sectionData =
		 * sectionServiceImpl.findByNameAndUnitId(sectionName, unitData.get().getId());
		 * if (sectionData.isEmpty()) { Section savedSection =
		 * makeAndSaveSection(sectionName, unitData.get()); makeAndSaveAsset(type, x, y,
		 * z, savedSection, assetsKKSTags); break; } Optional<Asset> assetData =
		 * assetServiceImpl.findByNameAndSectionId(assetsKKSTags,
		 * sectionData.get().getId()); if (assetData.isEmpty()) { makeAndSaveAsset(type,
		 * x, y, z, sectionData.get(), assetsKKSTags); break; } else if
		 * (!assetData.isEmpty()) { makeAndUpdateAsset(type, x, y, z, sectionData.get(),
		 * assetsKKSTags, assetData.get()); } } } workbook.close();
		 */}
}
