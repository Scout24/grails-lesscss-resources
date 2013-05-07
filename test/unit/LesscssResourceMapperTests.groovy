import org.gmock.WithGMock
import org.junit.Ignore
import org.junit.Test
import grails.test.GrailsUnitTestCase
import org.grails.plugin.resource.mapper.MapperPhase
import org.grails.plugin.resource.ResourceMeta
import org.lesscss.LessCompiler
import org.codehaus.groovy.grails.commons.GrailsApplication

/**
 * @author Paul Fairless
 *
 */

@WithGMock
class LesscssResourceMapperTests extends GrailsUnitTestCase {

  LesscssResourceMapper mapper
  def lessCompiler

  void setUp() {
    lessCompiler = mock(LessCompiler, constructor())
    mapper = new LesscssResourceMapper()
    mapper.grailsApplication = mock(GrailsApplication)
    mapper.metaClass.log = [debug: {}, error: {}]
  }

  @Test
  void testMapperGeneratesCssFromLessResource() {
    String fileName = "file.less"
    def targetFile = mock(File, constructor('/var/file/file.less.css'))
    targetFile.exists().returns(true).stub()

    def workDir = mock(File)
    def originalFile = mock(File, constructor(workDir, fileName))

    def processedFile = mock(File)
    processedFile.getName().returns(fileName).stub()
    processedFile.getAbsolutePath().returns('/var/file/' + fileName).stub()

    lessCompiler.compile(originalFile, targetFile).once()

    mapper.grailsApplication.getConfig().returns([:]).times(2)
    lessCompiler.setCompress(false).once()

    def config = [:]
    def resource = new ResourceMeta(contentType: '', tagAttributes: [rel: 'stylesheet/less'])
    def resourceMock = mock(resource)
    resourceMock.processedFile.returns(processedFile)

    resourceMock.workDir.returns(workDir)
    resourceMock.setProcessedFile(targetFile)
    resourceMock.sourceUrl.returns(fileName)
    resourceMock.updateActualUrlFromProcessedFile().once()

    def mockedMapper = mock(mapper)

    play {
      mapper = new LesscssResourceMapper()

      mockedMapper.map(resourceMock, config)
      assertEquals 'stylesheet', resource.tagAttributes.rel
      assertEquals 'text/css', resource.contentType
    }
  }

  @Test
  @Ignore
  void testLesscssCompression() {
    String fileName = "file.less"
    def targetFile = mock(File, constructor('/var/file/file.less.css'))
    targetFile.exists().returns(true).stub()

    def workDir = mock(File)
    def originalFile = mock(File, constructor(workDir, fileName))

    def processedFile = mock(File)
    processedFile.getName().returns(fileName).stub()
    processedFile.getAbsolutePath().returns('/var/file/' + fileName).stub()

    lessCompiler.compile(originalFile, targetFile).once()

    mapper.grailsApplication.getConfig().returns([grails: [resources: [mappers: [lesscss: [compress: true]]]]]).times(2)
    lessCompiler.setCompress(true).once()

    def config = [:]
    def resource = new ResourceMeta(contentType: '', tagAttributes: [rel: 'stylesheet/less'])
    def resourceMock = mock(resource)
    resourceMock.processedFile.returns(processedFile)
    resourceMock.workDir.returns(workDir)
    resourceMock.setProcessedFile(targetFile)
    resourceMock.sourceUrl.returns(fileName)
    resourceMock.updateActualUrlFromProcessedFile().once()

    def mockedMapper = mock(mapper)

    play {
      mapper = new LesscssResourceMapper()

      mockedMapper.map(resourceMock, config)
      assertEquals 'stylesheet', resource.tagAttributes.rel
      assertEquals 'text/css', resource.contentType
    }
  }

  @Test
  @Ignore
  void testMapperRunsEarlyInProcessingPipeline() {
    assertEquals MapperPhase.GENERATION, mapper.phase
  }

  @Test
  @Ignore
  void testMapperIncludesLessCSS() {
    assertEquals mapper.defaultIncludes, ['**/*.less']
  }

  @Test
  @Ignore
  void testGeneratedFilename() {
    assertEquals 'foo/bar.less.css', mapper.generateCompiledFileFromOriginal('foo/bar.less')
    assertEquals 'foo/bar.LESS.css', mapper.generateCompiledFileFromOriginal('foo/bar.LESS')
    assertEquals 'foo/./bar.less.css', mapper.generateCompiledFileFromOriginal('foo/./bar.less')
    assertEquals 'foo/less/bar.less.css', mapper.generateCompiledFileFromOriginal('foo/less/bar.less')
  }

}
